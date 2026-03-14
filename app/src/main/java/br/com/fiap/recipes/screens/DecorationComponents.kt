package br.com.fiap.recipes.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopEndCard(modifier: Modifier = Modifier) {
    Card(
        modifier
            .size(width = 160.dp, height = 85.dp),
        colors = CardDefaults
            .cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
        shape = RoundedCornerShape(bottomStart = 85.dp)
    ) { }
}

@Composable
fun BottomStartCard(modifier: Modifier = Modifier) {
    Card(
        modifier
            .size(width = 160.dp, height = 85.dp),
        colors = CardDefaults
            .cardColors(containerColor = MaterialTheme
                .colorScheme.primary),
        shape = RoundedCornerShape(topEnd = 85.dp)
    ) { }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TopEndCardPreview() {
    TopEndCard()
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BottomStartCardPreview() {
    BottomStartCard()
}