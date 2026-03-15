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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
                        stringResource(R.string.complete_the_assessment_to_view_tailored_recommendations)
                    } else {
                        stringResource(R.string.based_on_your_assessment_results_these_are_the_next_recommended_actions)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
                )
            }

            if (recommendations.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(R.string.no_recommendations_available),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = stringResource(R.string.start_the_maturity_assessment_to_generate_personalized_next_steps),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(recommendations) { recommendation ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = stringResource(recommendation.pillarTitle),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = stringResource(recommendation.priority),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = stringResource(recommendation.recommendation),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                text = stringResource(R.string.next_steps),
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
                            contentDescription = stringResource(R.string.user_profile_image),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(40.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.icon_circle_profile),
                            contentDescription = stringResource(R.string.default_profile_image),
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
    val homeTitle = stringResource(R.string.home)
    val assessmentTitle = stringResource(R.string.assessment)
    val dashboardTitle = stringResource(R.string.dashboard)
    val nextStepsTitle = stringResource(R.string.next_steps)

    val items = listOf(
        Pair(homeTitle, R.drawable.icon_home),
        Pair(assessmentTitle, R.drawable.icon_check),
        Pair(dashboardTitle, R.drawable.icon_dahsboard),
        Pair(nextStepsTitle, R.drawable.icon_next_step)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary
    ) {
        items.forEach { (title, icon) ->
            NavigationBarItem(
                selected = title == nextStepsTitle,
                onClick = {
                    when (title) {
                        homeTitle -> navController.navigate(Destination.HomeScreen.createRoute(email)) {
                            launchSingleTop = true
                        }

                        assessmentTitle -> navController.navigate(Destination.AssessmentScreen.createRoute(email)) {
                            launchSingleTop = true
                        }

                        dashboardTitle -> navController.navigate(Destination.DashboardScreen.createRoute(email)) {
                            launchSingleTop = true
                        }

                        nextStepsTitle -> navController.navigate(Destination.NextStepsScreen.createRoute(email)) {
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = title,
                        tint = if (title == nextStepsTitle) {
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
                        color = if (title == nextStepsTitle) {
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