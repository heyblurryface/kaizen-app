package br.com.fiap.kaizen.repository

import br.com.fiap.kaizen.R
import br.com.fiap.kaizen.model.AssessmentPillar
import br.com.fiap.kaizen.model.AssessmentQuestion

fun getAssessmentPillars(): List<AssessmentPillar> {
    return listOf(

        AssessmentPillar(
            id = 1,
            title = R.string.pillar_governance,
            questions = (1..6).map {
                AssessmentQuestion(it, getQuestionRes(it))
            }
        ),

        AssessmentPillar(
            id = 2,
            title = R.string.pillar_information_security,
            questions = (7..13).map {
                AssessmentQuestion(it, getQuestionRes(it))
            }
        ),

        AssessmentPillar(
            id = 3,
            title = R.string.pillar_ethics_integrity,
            questions = (14..19).map {
                AssessmentQuestion(it, getQuestionRes(it))
            }
        ),

        AssessmentPillar(
            id = 4,
            title = R.string.pillar_policies_documentation,
            questions = (20..25).map {
                AssessmentQuestion(it, getQuestionRes(it))
            }
        ),

        AssessmentPillar(
            id = 5,
            title = R.string.pillar_third_party_management,
            questions = (26..31).map {
                AssessmentQuestion(it, getQuestionRes(it))
            }
        )
    )
}

private fun getQuestionRes(id: Int): Int {
    return when (id) {
        1 -> R.string.q1
        2 -> R.string.q2
        3 -> R.string.q3
        4 -> R.string.q4
        5 -> R.string.q5
        6 -> R.string.q6
        7 -> R.string.q7
        8 -> R.string.q8
        9 -> R.string.q9
        10 -> R.string.q10
        11 -> R.string.q11
        12 -> R.string.q12
        13 -> R.string.q13
        14 -> R.string.q14
        15 -> R.string.q15
        16 -> R.string.q16
        17 -> R.string.q17
        18 -> R.string.q18
        19 -> R.string.q19
        20 -> R.string.q20
        21 -> R.string.q21
        22 -> R.string.q22
        23 -> R.string.q23
        24 -> R.string.q24
        25 -> R.string.q25
        26 -> R.string.q26
        27 -> R.string.q27
        28 -> R.string.q28
        29 -> R.string.q29
        30 -> R.string.q30
        31 -> R.string.q31
        else -> R.string.q1
    }
}