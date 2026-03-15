package br.com.fiap.kaizen.model


data class AssessmentPillar(
    val id: Int,
    val title: String,
    val questions: List<AssessmentQuestion>
)