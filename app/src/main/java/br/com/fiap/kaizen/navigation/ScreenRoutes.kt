package br.com.fiap.kaizen.navigation

sealed class Destination(val route: String){

    object InitialScreen: Destination("initial")
    object SignupScreen: Destination("signup")

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

    object CompanyScreen : Destination("company/{email}") {
        fun createRoute(email: String): String {
            return "company/$email"
        }
    }
    object AssessmentScreen: Destination("assessment/{email}"){
        fun createRoute(email: String): String {
            return "assessment/$email"
        }
    }
    object DashboardScreen: Destination("dashboard/{email}"){
        fun createRoute(email: String): String {
            return "dashboard/$email"
        }
    }
    object NextStepsScreen : Destination("nextsteps/{email}") {
        fun createRoute(email: String): String {
            return "nextsteps/$email"
        }
    }
}