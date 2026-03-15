package br.com.fiap.kaizen.repository

import br.com.fiap.kaizen.R

fun getCompanySizeOptions(): List<Int> {
    return listOf(
        R.string.company_size_micro,
        R.string.company_size_small,
        R.string.company_size_medium,
        R.string.company_size_large,
        R.string.company_size_enterprise
    )
}

fun getCriticalThirdPartyOptions(): List<Int> {
    return listOf(
        R.string.option_yes,
        R.string.option_no,
        R.string.option_not_sure
    )
}