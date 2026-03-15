package br.com.fiap.kaizen.model

import androidx.annotation.StringRes


data class AssessmentPillar(
    val id: Int,
    @StringRes val title: Int,
    val questions: List<AssessmentQuestion>
)