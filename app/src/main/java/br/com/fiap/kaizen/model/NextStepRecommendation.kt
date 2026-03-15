package br.com.fiap.kaizen.model

import androidx.annotation.StringRes

data class NextStepRecommendation(
    @StringRes val pillarTitle: Int,
    @StringRes val priority: Int,
    @StringRes val recommendation: Int
)