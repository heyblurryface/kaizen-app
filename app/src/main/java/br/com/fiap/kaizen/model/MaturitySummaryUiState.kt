package br.com.fiap.kaizen.model

data class MaturitySummaryUiState(
    val status: AssessmentStatus,
    val level: Int? = null,
    val maturityLabel: String,
    val progress: Float,
    val insight: String
)
