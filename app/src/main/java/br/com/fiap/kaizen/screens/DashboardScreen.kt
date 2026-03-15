package br.com.fiap.kaizen.screens

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.kaizen.R
import br.com.fiap.kaizen.model.AssessmentResponse
import br.com.fiap.kaizen.model.User
import br.com.fiap.kaizen.navigation.Destination
import br.com.fiap.kaizen.repository.RoomAssessmentResponseRepository
import br.com.fiap.kaizen.repository.RoomUserRepository
import br.com.fiap.kaizen.repository.UserRepository
import br.com.fiap.kaizen.ui.theme.KaizenTheme
import br.com.fiap.kaizen.utils.convertByteArrayToBitmap

data class DashboardPillarResult(
    val pillarTitle: String,
    val score: Int,
    val maxScore: Int,
    val percentage: Float
)


@Composable
fun DashboardScreen(email: String, navController: NavController) {
    val context = LocalContext.current
    val repository = RoomAssessmentResponseRepository(context)
    val responses = remember { repository.getAllResponses() }

    val totalScore = responses.sumOf { it.answerScore }
    val maxScore = responses.size * 3
    val totalPercentage = if (maxScore > 0) totalScore.toFloat() / maxScore else 0f

    val pillarResults = remember(responses) {
        responses
            .groupBy { it.pillarTitle }
            .map { (pillarTitle, pillarResponses) ->
                val score = pillarResponses.sumOf { it.answerScore }
                val pillarMax = pillarResponses.size * 3
                DashboardPillarResult(
                    pillarTitle = pillarTitle,
                    score = score,
                    maxScore = pillarMax,
                    percentage = if (pillarMax > 0) score.toFloat() / pillarMax else 0f
                )
            }
            .sortedBy { it.pillarTitle }
    }

    val strongestPillar = pillarResults.maxByOrNull { it.percentage }
    val weakestPillar = pillarResults.minByOrNull { it.percentage }

    val maturityLevel = when {
        totalPercentage <= 0.25f -> "Initial"
        totalPercentage <= 0.50f -> "Developing"
        totalPercentage <= 0.75f -> "Structured"
        else -> "Advanced"
    }

    Scaffold(
        topBar = {
            DashboardTopBar(email = email, navController = navController)
        },
        bottomBar = {
            DashboardBottomBar(
                navController = navController,
                email = email
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Overall Maturity",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = maturityLevel,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2B2D)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "$totalScore / $maxScore points",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF4F4F4F)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { totalPercentage },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = Color(0xFFEAEAEA)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Pillar Performance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2B2D)
                )
            }

            items(pillarResults.size) { index ->
                val pillar = pillarResults[index]

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = pillar.pillarTitle,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F2B2D)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${pillar.score} / ${pillar.maxScore} points",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4F4F4F)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { pillar.percentage },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = Color(0xFFEAEAEA)
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Insights",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2B2D)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Strongest pillar: ${strongestPillar?.pillarTitle ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Most critical pillar: ${weakestPillar?.pillarTitle ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(email: String = "", navController: NavController) {
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
                text = "Dashboard",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        },
        actions = {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
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
fun DashboardBottomBar(
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
                selected = item.title == "Dashboard",
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
                            // quando criar a rota no Destination, plugar aqui
                        }

                        "Next Steps" -> {
                            // depois
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (item.title == "Dashboard")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.displaySmall,
                        color = if (item.title == "Dashboard")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun DashboardScreenPreview() {
    KaizenTheme {
        DashboardScreen(
            email = "preview@example.com",
            navController = rememberNavController()
        )
    }
}