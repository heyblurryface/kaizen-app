package br.com.fiap.kaizen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.fiap.kaizen.navigation.NavigationRoutes
import br.com.fiap.kaizen.ui.theme.KaizenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KaizenTheme {
                NavigationRoutes()
            }
        }
    }
}