package br.com.fiap.kaizen.screens

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.kaizen.R
import br.com.fiap.kaizen.model.User
import br.com.fiap.kaizen.navigation.Destination
import br.com.fiap.kaizen.repository.RoomAssessmentResponseRepository
import br.com.fiap.kaizen.repository.RoomUserRepository
import br.com.fiap.kaizen.repository.UserRepository
import br.com.fiap.kaizen.repository.getNextStepRecommendations
import br.com.fiap.kaizen.ui.theme.KaizenTheme
import br.com.fiap.kaizen.utils.convertByteArrayToBitmap

@Composable
fun NextStepsScreen(email: String, navController: NavController) {
    val context = LocalContext.current
    val repository = RoomAssessmentResponseRepository(context)
    val responses = remember { repository.getAllResponses() }
    val recommendations = remember(responses) { getNextStepRecommendations(responses) }

    Scaffold(
        topBar = {
            NextStepsTopBar(email = email, navController = navController)
        },
        bottomBar = {
            NextStepsBottomBar(email = email, navController = navController)
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
                 Text(
                    text = if (recommendations.isEmpty()) {
                        "Complete the assessment to view tailored recommendations."
                    } else {
                        "Based on your assessment results, these are the next recommended actions."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4F4F4F)
                )
            }

            if (recommendations.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "No recommendations available",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2B2D)
                            )
                            Text(
                                text = "Start the maturity assessment to generate personalized next steps.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF4F4F4F)
                            )
                        }
                    }
                }
            } else {
                items(recommendations) { recommendation ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = recommendation.pillarTitle,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2B2D),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = recommendation.priority,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = recommendation.recommendation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF4F4F4F)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextStepsTopBar(email: String = "", navController: NavController) {
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
                text = "Next Steps",
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
                        navController.navigate(
                            Destination.ProfileScreen.createRoute(email)
                        ) {
                            launchSingleTop = true
                        }
                    }
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "User profile image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(40.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.icon_circle_profile),
                            contentDescription = "Default profile image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun NextStepsBottomBar(email: String, navController: NavController) {
    val items = listOf(
        Pair("Home", R.drawable.icon_home),
        Pair("Assessment", R.drawable.icon_check),
        Pair("Dashboard", R.drawable.icon_dahsboard),
        Pair("Next Steps", R.drawable.icon_next_step)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary
    ) {
        items.forEach { (title, icon) ->
            NavigationBarItem(
                selected = title == "Next Steps",
                onClick = {
                    when (title) {
                        "Home" -> navController.navigate(Destination.HomeScreen.createRoute(email)) {
                            launchSingleTop = true
                        }

                        "Assessment" -> navController.navigate(Destination.AssessmentScreen.createRoute(email)) {
                            launchSingleTop = true
                        }

                        "Dashboard" -> navController.navigate(Destination.DashboardScreen.createRoute(email)) {
                            launchSingleTop = true
                        }

                        "Next Steps" -> navController.navigate(Destination.NextStepsScreen.createRoute(email)) {
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = title,
                        tint = if (title == "Next Steps") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onTertiary
                        },
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.displaySmall,
                        color = if (title == "Next Steps") {
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun NextStepsScreenPreview() {
    KaizenTheme {
        NextStepsScreen(
            email = "preview@example.com",
            navController = rememberNavController()
        )
    }
}