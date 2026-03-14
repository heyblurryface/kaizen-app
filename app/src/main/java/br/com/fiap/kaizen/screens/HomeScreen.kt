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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.kaizen.R
import br.com.fiap.kaizen.components.CategoryItem
import br.com.fiap.kaizen.components.RecipeItem
import br.com.fiap.kaizen.model.AssessmentStatus
import br.com.fiap.kaizen.model.MaturitySummaryUiState
import br.com.fiap.kaizen.model.User
import br.com.fiap.kaizen.navigation.Destination
import br.com.fiap.kaizen.repository.RoomUserRepository
import br.com.fiap.kaizen.repository.UserRepository
import br.com.fiap.kaizen.repository.getAllCategories
import br.com.fiap.kaizen.repository.getAllRecipes
import br.com.fiap.kaizen.ui.theme.KaizenTheme
import br.com.fiap.kaizen.utils.convertByteArrayToBitmap

@Composable
fun HomeScreen(email: String, navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                MyTopAppBar(email, navController)
            },
            bottomBar = {
                MyBottomAppBar()
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {},
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

// TRECHO DE CÓDIGO OMITIDO
// *** Conteúdo da Tela
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: MaturitySummaryUiState,
    onDetailsClick: () -> Unit
) {

    val categories = getAllCategories();

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
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 0.dp) // modificado
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

                Text(
                    text = "More details →",
                    modifier = Modifier.clickable(onClick = onDetailsClick),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Text(
            text = "Maturity Pillars",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp) // modificado
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onClick = {
                        navController.navigate(
                            route = Destination
                                .CategoryRecipeScreen
                                .createRoute(id = category.id)
                        )
                    }
                )
            }
        }
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

// TRECHO DE CÓDIGO OMITIDO...
// *** TopAppBar ***
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(email: String = "", navController: NavController) {

    val user: User? = if (androidx.compose.ui.platform.LocalInspectionMode.current) {
        User(name = "Preview User", email = "preview@example.com")
    } else {
        val userRepository: UserRepository = RoomUserRepository(LocalContext.current)
        userRepository.getUserByEmail(email)
    }

    // variáveis de estado para exibir a imagem do usuário
    var bitmap by remember {
        mutableStateOf<Bitmap?>(
            if (user?.userImage != null) convertByteArrayToBitmap(user.userImage) else null
        )
    }

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
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
                        text = user?.email ?: "No email",
                        style = MaterialTheme.typography.displaySmall
                    )
                }
                Card(
                    shape = CircleShape,
                    colors = CardDefaults
                        .cardColors(
                            containerColor = Color.Transparent
                        ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.size(48.dp)
                        .clickable(
                            onClick = { user?.let { navController.navigate("profile/${it.email}") } }
                        )
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

// TRECHO DE CÓDIGO OMITIDO...
// *** BottomAppBar
data class BottomNavigationItem(
    val title: String,
    val icon: Int
)

@Composable
fun MyBottomAppBar(modifier: Modifier = Modifier) {
    val items = listOf(
        BottomNavigationItem(title = "Home", icon = R.drawable.icon_home),
        BottomNavigationItem("Assessment", icon = R.drawable.icon_check),
        BottomNavigationItem("Dashboard", icon = R.drawable.icon_dahsboard),
        BottomNavigationItem("Next Steps", icon = R.drawable.icon_next_step)
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
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
        MyBottomAppBar()
    }
}
