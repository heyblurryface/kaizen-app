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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import br.com.fiap.kaizen.ui.theme.KaizenDark
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
            MyBottomAppBar(
                navController = navController,
                email = email,
                selectedItemRes = R.string.assessment
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.maturity_assessment),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.answer_the_questions_to_identify_the_initial_maturity_level),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
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
                            showErrorDialog = context.getString(
                                R.string.there_are_still_unanswered_questions,
                                unanswered
                            )
                        } else {
                            try {
                                val responses = pillars.flatMap { pillar ->
                                    pillar.questions.map { question ->
                                        AssessmentResponse(
                                            questionId = question.id,
                                            pillarId = pillar.id,
                                            pillarTitle = context.getString(pillar.title),
                                            questionText = context.getString(question.text),
                                            answerScore = question.answer ?: 0
                                        )
                                    }
                                }

                                assessmentRepository.deleteAllResponses()
                                assessmentRepository.saveAllResponses(responses)

                                showSuccessDialog = true
                            } catch (_: Exception) {
                                showErrorDialog =
                                    context.getString(R.string.error_saving_assessment_responses)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else KaizenDark,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.save_assessment),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            title = {
                Text(stringResource(R.string.success))
            },
            text = {
                Text(stringResource(R.string.assessment_saved_successfully))
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate(Destination.HomeScreen.createRoute(email)) {
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }

    if (showErrorDialog != null) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = null },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            title = {
                Text(stringResource(R.string.incomplete_assessment))
            },
            text = {
                Text(showErrorDialog!!)
            },
            confirmButton = {
                Button(onClick = { showErrorDialog = null }) {
                    Text(stringResource(R.string.ok))
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
                text = stringResource(R.string.maturity_assessment),
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
                            contentDescription = stringResource(R.string.user_profile_image),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.icon_circle_profile),
                            contentDescription = stringResource(R.string.default_profile_image),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    )
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