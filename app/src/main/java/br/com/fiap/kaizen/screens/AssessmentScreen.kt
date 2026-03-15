package br.com.fiap.kaizen.screens

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.kaizen.R
import br.com.fiap.kaizen.components.AssessmentPillarSection
import br.com.fiap.kaizen.model.AssessmentResponse
import br.com.fiap.kaizen.model.User
import br.com.fiap.kaizen.navigation.Destination
import br.com.fiap.kaizen.repository.RoomAssessmentResponseRepository
import br.com.fiap.kaizen.repository.RoomUserRepository
import br.com.fiap.kaizen.repository.UserRepository
import br.com.fiap.kaizen.repository.getAssessmentPillars
import br.com.fiap.kaizen.ui.theme.KaizenTheme
import br.com.fiap.kaizen.utils.convertByteArrayToBitmap

@Composable
fun AssessmentScreen(email: String, navController: NavController) {
    val context = LocalContext.current
    val assessmentRepository = RoomAssessmentResponseRepository(context)

    val pillars = remember { getAssessmentPillars() }

    val expandedStates = remember(pillars.size) {
        mutableStateListOf<Boolean>().apply {
            repeat(pillars.size) {
                add(false)
            }
        }
    }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            AssessmentTopBar(
                email = email,
                navController = navController
            )
        },
        bottomBar = {
            AssessmentBottomBar(
                navController = navController,
                email = email
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Answer the questions to identify the initial maturity level.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4F4F4F)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            itemsIndexed(pillars) { index, pillar ->
                AssessmentPillarSection(
                    pillar = pillar,
                    expanded = expandedStates[index],
                    onToggle = {
                        expandedStates[index] = !expandedStates[index]
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val allQuestions = pillars.flatMap { it.questions }
                        val unanswered = allQuestions.count { it.answer == null }

                        if (unanswered > 0) {
                            showErrorDialog = "There are still $unanswered unanswered questions."
                        } else {
                            try {
                                val responses = pillars.flatMap { pillar ->
                                    pillar.questions.map { question ->
                                        AssessmentResponse(
                                            questionId = question.id,
                                            pillarId = pillar.id,
                                            pillarTitle = pillar.title,
                                            questionText = question.text,
                                            answerScore = question.answer ?: 0
                                        )
                                    }
                                }

                                assessmentRepository.deleteAllResponses()
                                assessmentRepository.saveAllResponses(responses)

                                showSuccessDialog = true
                            } catch (e: Exception) {
                                showErrorDialog = "Error saving assessment responses."
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Save Assessment")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Success") },
            text = { Text("Assessment saved successfully.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate(Destination.HomeScreen.createRoute(email)) {
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    if (showErrorDialog != null) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = null },
            title = { Text("Incomplete Assessment") },
            text = { Text(showErrorDialog!!) },
            confirmButton = {
                Button(onClick = { showErrorDialog = null }) {
                    Text("OK")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentTopBar(email: String = "", navController: NavController) {
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current

    val user: User? = if (isPreview) {
        User(name = "Preview User", email = "preview@example.com")
    } else {
        val userRepository: UserRepository = RoomUserRepository(context)
        userRepository.getUserByEmail(email)
    }

    val bitmap: Bitmap? = remember(user) {
        if (user?.userImage != null) convertByteArrayToBitmap(user.userImage) else null
    }

    TopAppBar(
        title = {
            Text(
                text = "Maturity Assessment",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        },
        actions = {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.padding(end = 12.dp)
            ) {
                IconButton(
                    onClick = {
                        user?.let {
                            navController.navigate(
                                Destination.ProfileScreen.createRoute(it.email)
                            )
                        }
                    }
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "User profile image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.icon_circle_profile),
                            contentDescription = "Default profile image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun AssessmentBottomBar(
    navController: NavController,
    email: String
) {
    val items = listOf(
        BottomNavigationItem(title = "Home", icon = R.drawable.icon_home),
        BottomNavigationItem(title = "Assessment", icon = R.drawable.icon_check),
        BottomNavigationItem(title = "Dashboard", icon = R.drawable.icon_dahsboard),
        BottomNavigationItem(title = "Next Steps", icon = R.drawable.icon_next_step)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = item.title == "Assessment",
                onClick = {
                    when (item.title) {
                        "Home" -> {
                            navController.navigate(Destination.HomeScreen.createRoute(email)) {
                                launchSingleTop = true
                            }
                        }

                        "Assessment" -> {
                            navController.navigate(Destination.AssessmentScreen.createRoute(email)) {
                                launchSingleTop = true
                            }
                        }

                        "Dashboard" -> {
                            navController.navigate(Destination.DashboardScreen.createRoute(email)) {
                                launchSingleTop = true
                            }
                        }

                        "Next Steps" -> {
                            navController.navigate(Destination.NextStepsScreen.createRoute(email)) {
                                launchSingleTop = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (item.title == "Assessment") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onTertiary
                        },
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.displaySmall,
                        color = if (item.title == "Assessment") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        }
                    )
                }
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun AssessmentScreenPreview() {
    KaizenTheme {
        AssessmentScreen(
            email = "preview@example.com",
            navController = rememberNavController()
        )
    }
}