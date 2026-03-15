package br.com.fiap.kaizen.repository

fun getCompanySizeOptions(): List<String> {
    return listOf(
        "Micro",
        "Small",
        "Medium",
        "Large",
        "Enterprise"
    )
}

fun getCriticalThirdPartyOptions(): List<String> {
    return listOf(
        "Yes",
        "No",
        "Not Sure"
    )
}