package br.com.fiap.recipes.screens

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.recipes.R
import br.com.fiap.recipes.components.CategoryItem
import br.com.fiap.recipes.components.RecipeItem
import br.com.fiap.recipes.navigation.Destination
import br.com.fiap.recipes.repository.RoomUserRepository
import br.com.fiap.recipes.repository.UserRepository
import br.com.fiap.recipes.repository.getAllCategories
import br.com.fiap.recipes.repository.getAllRecipes
import br.com.fiap.recipes.ui.theme.RecipesTheme
import br.com.fiap.recipes.utils.convertByteArrayToBitmap

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
                FloatingActionButton(
                    onClick = {},
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        ) { paddingValues ->
            ContentScreen(
                modifier = Modifier.padding(paddingValues),
                navController = navController
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
    RecipesTheme {
        //HomeScreen("")
    }
}

// TRECHO DE CÓDIGO OMITIDO
// *** Conteúdo da Tela
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val categories = getAllCategories();
    val recipes = getAllRecipes()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 0.dp) // modificado
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp), //modificado
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color.LightGray,
                ),
            trailingIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = ""
                    )
                }
            },
            placeholder = {
                Text(text = stringResource(R.string.search_by_recipes))
            }
        )
        //Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp) // modificado
                .fillMaxWidth()
                .height(112.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.cooking_card),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = "Categories",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp) // modificado
        )
        //Spacer(modifier = Modifier.height(16.dp))
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
        //Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Newly added recipes",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 16.dp,
                horizontal = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recipes) { recipe ->
                RecipeItem(recipe)
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
    RecipesTheme {
        //ContentScreen()
    }
}

// TRECHO DE CÓDIGO OMITIDO...
// *** TopAppBar ***
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(email: String = "", navController: NavController) {

    // Criar uma instância da classe SharedPreferencesUserRepository
    //val userRepository: UserRepository = SharedPreferencesUserRepository(LocalContext.current)
    val userRepository: UserRepository = RoomUserRepository(LocalContext.current)
    val user = userRepository.getUserByEmail(email)

    // variáveis de estado para exibir a imagem do usuário
    var bitmap by remember {
        mutableStateOf<Bitmap?>(
            convertByteArrayToBitmap(user!!.userImage!!)
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
                        text = user!!.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user.email,
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
                            onClick = { navController.navigate("profile/${user!!.email}")}
                        )
                ) {
                    Image(
                        //painter = painterResource(R.drawable.user),
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "",
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
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
    RecipesTheme {
        //MyTopAppBar("", navController = navController)
    }
}

// TRECHO DE CÓDIGO OMITIDO...
// *** BottomAppBar
data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun MyBottomAppBar(modifier: Modifier = Modifier) {
    val items = listOf(
        BottomNavigationItem(title = "Home", icon = Icons.Default.Home),
        BottomNavigationItem("Favorites", Icons.Default.Favorite),
        BottomNavigationItem("Profile", Icons.Default.Person)
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onTertiary
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
    RecipesTheme {
        MyBottomAppBar()
    }
}