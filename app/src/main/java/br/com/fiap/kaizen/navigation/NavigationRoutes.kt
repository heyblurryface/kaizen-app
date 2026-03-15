package br.com.fiap.kaizen.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import br.com.fiap.kaizen.screens.AssessmentScreen
import br.com.fiap.kaizen.screens.CategoryRecipeScreen
import br.com.fiap.kaizen.screens.CompanyScreen
import br.com.fiap.kaizen.screens.HomeScreen
import br.com.fiap.kaizen.screens.InitialScreen
import br.com.fiap.kaizen.screens.LoginScreen
import br.com.fiap.kaizen.screens.ProfileScreen
import br.com.fiap.kaizen.screens.SignupScreen

@Composable
fun NavigationRoutes() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.InitialScreen.route
    ) {
        composable(Destination.InitialScreen.route) {
            InitialScreen(navController)
        }

        composable(
            route = Destination.HomeScreen.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://recipes.fiap.com.br/email/{email}"
                    action = Intent.ACTION_VIEW
                }
            ),
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            HomeScreen(email!!, navController)
        }

        composable(
            route = Destination.CategoryRecipeScreen.route,
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("id")
            CategoryRecipeScreen(categoryId, navController)
        }

        composable(Destination.SignupScreen.route) {
            SignupScreen(navController)
        }

        composable(
            route = Destination.ProfileScreen.route,
            arguments = listOf(
                navArgument(name = "email") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            ProfileScreen(navController, email!!)
        }

        composable(Destination.LoginScreen.route) {
            LoginScreen(navController)
        }

        composable(Destination.CompanyScreen.route) {
            CompanyScreen(navController)
        }

        composable(
            route = Destination.AssessmentScreen.route,
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            AssessmentScreen(email = email!!, navController = navController)
        }
    }
}