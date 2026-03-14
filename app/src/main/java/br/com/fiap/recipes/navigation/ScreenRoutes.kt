package br.com.fiap.recipes.navigation

sealed class Destination(val route: String){
    object InitialScreen: Destination("initial")
    object SignupScreen: Destination("signup")

    //object ProfileScreen: Destination("profile")
    object ProfileScreen: Destination("profile/{email}"){
        fun createRoute(email: String): String{
            return "profile/$email"
        }
    }

    object HomeScreen: Destination("home/{email}"){
        fun createRoute(email: String): String {
            return "home/$email"
        }
    }

    object CategoryRecipeScreen: Destination("categoryRecipes/{id}"){
        fun createRoute(id: Int): String {
            return "categoryRecipes/$id"
        }
    }

    object LoginScreen: Destination("login")
}