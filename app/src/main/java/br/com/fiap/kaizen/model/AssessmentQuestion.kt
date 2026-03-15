package br.com.fiap.kaizen.model

import androidx.annotation.StringRes

data class AssessmentQuestion(
    val id: Int,
    @StringRes val text: Int,
    var answer: Int? = null
)