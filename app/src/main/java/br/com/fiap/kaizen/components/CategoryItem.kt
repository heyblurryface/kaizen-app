package br.com.fiap.kaizen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.kaizen.model.Category
import br.com.fiap.kaizen.ui.theme.KaizenTheme

@Composable
fun CategoryItem(
    category: Category = Category(),
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(96.dp)
            .clickable(onClick = onClick),
    ) {
        Card(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(88.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = category.background
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(category.image!!),
                    contentDescription = category.name,
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        Text(
            text = category.name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp,
            modifier = Modifier.height(40.dp)
        )
    }
}

@Preview
@Composable
private fun CategoryItemPreview() {
    KaizenTheme {
        CategoryItem(onClick = {})
    }
}