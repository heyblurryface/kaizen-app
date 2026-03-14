package br.com.fiap.kaizen.repository

import androidx.compose.ui.graphics.Color
import br.com.fiap.kaizen.R
import br.com.fiap.kaizen.model.Category

fun getAllCategories() = listOf<Category>(
    Category(id = 1000, name = "Governance",
        image = R.drawable.governance, background = Color(0xFFD9D9D9)
    ),
    Category(id = 2000, name = "Cyber Security",
        image = R.drawable.security, background = Color(0xFFD9D9D9)),
    Category(id = 3000, name = "Ethics & Integrity",
        image = R.drawable.verified, background = Color(0xFFD9D9D9)),
    Category(id = 4000, name = "Policies & Procedures",
        image = R.drawable.description, background = Color(0xFFD9D9D9)),
    Category(id = 5000, name = "Third-Party Management",
        image = R.drawable.third_party, background = Color(0xFFD9D9D9)))

fun getCategoryById(id: Int) = getAllCategories()
    .find { category ->
        category.id == id
    }