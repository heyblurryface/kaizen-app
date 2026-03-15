package br.com.fiap.kaizen.screens

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import br.com.fiap.kaizen.model.AssessmentStatus
import br.com.fiap.kaizen.model.MaturitySummaryUiState
import br.com.fiap.kaizen.model.User
import br.com.fiap.kaizen.navigation.Destination
import br.com.fiap.kaizen.repository.RoomCompanyRepository
import br.com.fiap.kaizen.repository.RoomUserRepository
import br.com.fiap.kaizen.repository.UserRepository
import br.com.fiap.kaizen.ui.theme.KaizenTheme
import br.com.fiap.kaizen.utils.convertByteArrayToBitmap
import br.com.fiap.kaizen.repository.RoomAssessmentResponseRepository

@Composable
fun HomeScreen(email: String, navController: NavController) {
    val context = LocalContext.current
    val assessmentRepository = RoomAssessmentResponseRepository(context)

    val responses = remember { assessmentRepository.getAllResponses() }

    val totalScore = responses.sumOf { it.answerScore }
    val maxScore = responses.size * 3

    val homeUiState = remember(responses) {
        calculateHomeUiState(
            totalScore = totalScore,
            maxScore = maxScore
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                MyTopAppBar(email, navController)
            },
            bottomBar = {
                MyBottomAppBar(
                    navController = navController,
                    email = email,
                    selectedItem = "Home"
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate(
                            Destination.AssessmentScreen.createRoute(email)
                        ) {
                            launchSingleTop = true
                        }
                    },
                    shape = RoundedCornerShape(50),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Start Assessment"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Start Assessment",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        ) { paddingValues ->
            ContentScreen(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                email = email,
                uiState = homeUiState,
                onDetailsClick = {
                    navController.navigate(
                        Destination.DashboardScreen.createRoute(email)
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Composable
private fun HomeScreenPreview() {
    KaizenTheme {
        HomeScreen("", navController = rememberNavController())
    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    email: String,
    uiState: MaturitySummaryUiState,
    onDetailsClick: () -> Unit
) {
    val badgeColor = when (uiState.status) {
        AssessmentStatus.NOT_STARTED -> Color(0xFFBDBDBD)
        AssessmentStatus.IN_PROGRESS -> Color(0xFFE6A23C)
        AssessmentStatus.COMPLETED -> Color(0xFFE6A23C)
    }

    val progressColor = when (uiState.status) {
        AssessmentStatus.NOT_STARTED -> Color(0xFFD9D9D9)
        AssessmentStatus.IN_PROGRESS -> Color(0xFFE6A23C)
        AssessmentStatus.COMPLETED -> MaterialTheme.colorScheme.primary
    }

    val badgeText = when (uiState.status) {
        AssessmentStatus.NOT_STARTED -> "NOT STARTED"
        AssessmentStatus.IN_PROGRESS -> "IN PROGRESS"
        AssessmentStatus.COMPLETED -> "LEVEL ${uiState.level ?: "-"}"
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Current maturity",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = badgeColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = uiState.maturityLabel,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2B2D)
                )

                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = { uiState.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(99.dp)),
                    color = progressColor,
                    trackColor = Color(0xFFEAEAEA)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = uiState.insight,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4F4F4F)
                )

                Spacer(modifier = Modifier.height(14.dp))

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(84.dp)
                    .clickable {
                        navController.navigate(Destination.CompanyScreen.route)
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = "Company Profile",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Company Profile",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF1F2B2D),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(84.dp)
                    .clickable {
                        navController.navigate("latest_report")
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .clickable {
                            navController.navigate(
                                Destination.DashboardScreen.createRoute(email)
                            ) {
                                launchSingleTop = true
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Latest Report",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Latest Report",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF1F2B2D),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Composable
private fun ContentScreenPreview() {
    KaizenTheme {
        ContentScreen(
            navController = rememberNavController(),
            email = "preview@example.com",
            uiState = MaturitySummaryUiState(
                status = AssessmentStatus.IN_PROGRESS,
                maturityLabel = "Initial",
                progress = 0.2f,
                insight = "Keep going!"
            ),
            onDetailsClick = {}
        )
    }
}

private fun calculateHomeUiState(totalScore: Int, maxScore: Int): MaturitySummaryUiState {
    if (maxScore == 0) {
        return MaturitySummaryUiState(
            status = AssessmentStatus.NOT_STARTED,
            maturityLabel = "Not available",
            progress = 0f,
            insight = "Get started"
        )
    }

    val percentage = totalScore.toFloat() / maxScore.toFloat()

    val maturityLevel = when {
        percentage <= 0.25f -> "Initial"
        percentage <= 0.50f -> "Developing"
        percentage <= 0.75f -> "Structured"
        else -> "Advanced"
    }

    val level = when {
        percentage <= 0.25f -> 1
        percentage <= 0.50f -> 2
        percentage <= 0.75f -> 3
        else -> 4
    }

    return MaturitySummaryUiState(
        status = AssessmentStatus.COMPLETED,
        level = level,
        maturityLabel = maturityLevel,
        progress = percentage,
        insight = "$totalScore / $maxScore points"
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyTopAppBar(email: String = "", navController: NavController) {
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current

    val user: User? = if (isPreview) {
        User(name = "Preview User", email = "preview@example.com")
    } else {
        val userRepository: UserRepository = RoomUserRepository(context)
        userRepository.getUserByEmail(email)
    }

    val company = if (isPreview) {
        null
    } else {
        val companyRepository = RoomCompanyRepository(context)
        companyRepository.getLastCompany()
    }

    var bitmap by remember {
        mutableStateOf<Bitmap?>(
            if (user?.userImage != null) convertByteArrayToBitmap(user.userImage) else null
        )
    }

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = user?.name ?: "No name",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (company != null)
                            "${company.companyName} - ${company.role}"
                        else
                            "No company registered",
                        style = MaterialTheme.typography.displaySmall
                    )
                }

                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            user?.let {
                                navController.navigate(
                                    Destination.ProfileScreen.createRoute(it.email)
                                )
                            }
                        }
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = "",
                            alignment = Alignment.Center,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.icon_circle_profile),
                            contentDescription = "",
                            alignment = Alignment.Center,
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
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Composable
private fun MyTopAppBarPreview() {
    KaizenTheme {
        MyTopAppBar("", navController = rememberNavController())
    }
}

data class BottomNavigationItem(
    val title: String,
    val icon: Int
)

@Composable
fun MyBottomAppBar(
    navController: NavController,
    email: String,
    selectedItem: String,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavigationItem(title = "Home", icon = R.drawable.icon_home),
        BottomNavigationItem(title = "Assessment", icon = R.drawable.icon_check),
        BottomNavigationItem(title = "Dashboard", icon = R.drawable.icon_dahsboard),
        BottomNavigationItem(title = "Next Steps", icon = R.drawable.icon_next_step)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item.title,
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
                            // depois você pluga a rota real
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (selectedItem == item.title)
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
                        color = if (selectedItem == item.title)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Composable
private fun MyBottomAppBarPreview() {
    KaizenTheme {
        MyBottomAppBar(
            navController = rememberNavController(),
            email = "preview@example.com",
            selectedItem = "Home"
        )
    }
}