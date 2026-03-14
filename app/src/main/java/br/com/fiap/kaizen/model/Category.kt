package br.com.fiap.kaizen.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import br.com.fiap.kaizen.R

data class Category(
    val id: Int = 0,
    val name: String = "Name",
    @DrawableRes val image: Int? = R.drawable.no_photo,
    val background: Color = Color.Gray
)
