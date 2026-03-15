package br.com.fiap.kaizen.model

import androidx.annotation.StringRes
import br.com.fiap.kaizen.R

enum class DifficultLevel(@StringRes val descriptionRes: Int) {
    BEGINNER(R.string.beginner),
    INTERMEDIATE(R.string.intermediate),
    ADVANCED(R.string.advanced)
}