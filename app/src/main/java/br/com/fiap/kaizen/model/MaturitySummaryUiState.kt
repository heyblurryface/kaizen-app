package br.com.fiap.kaizen.model

import androidx.annotation.StringRes

data class MaturitySummaryUiState(
    val status: AssessmentStatus,
    val level: Int? = null,
    @StringRes val maturityLabel: Int,
    val progress: Float,
    @StringRes val insight: Int,
    val insightScore: Int? = null,
    val insightMaxScore: Int? = null
)