package br.com.fiap.kaizen.model

data class AssessmentQuestion(
    val id: Int,
    val text: String,
    var answer: Int? = null
)