package br.com.fiap.kaizen.repository

import br.com.fiap.kaizen.R
import br.com.fiap.kaizen.model.AssessmentResponse
import br.com.fiap.kaizen.model.NextStepRecommendation

fun getNextStepRecommendations(
    responses: List<AssessmentResponse>
): List<NextStepRecommendation> {
    if (responses.isEmpty()) return emptyList()

    val pillarResults = responses
        .groupBy { it.pillarId }
        .map { (pillarId, pillarResponses) ->
            val score = pillarResponses.sumOf { it.answerScore }
            val maxScore = pillarResponses.size * 3
            val percentage = if (maxScore > 0) score.toFloat() / maxScore else 0f

            when (pillarId) {
                1 -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_governance,
                        priority = R.string.priority_high,
                        recommendation = R.string.rec_governance_high
                    )

                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_governance,
                        priority = R.string.priority_medium,
                        recommendation = R.string.rec_governance_medium
                    )

                    else -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_governance,
                        priority = R.string.priority_maintain,
                        recommendation = R.string.rec_governance_maintain
                    )
                }

                2 -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_information_security,
                        priority = R.string.priority_high,
                        recommendation = R.string.rec_security_high
                    )

                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_information_security,
                        priority = R.string.priority_medium,
                        recommendation = R.string.rec_security_medium
                    )

                    else -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_information_security,
                        priority = R.string.priority_maintain,
                        recommendation = R.string.rec_security_maintain
                    )
                }

                3 -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_ethics_integrity,
                        priority = R.string.priority_high,
                        recommendation = R.string.rec_ethics_high
                    )

                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_ethics_integrity,
                        priority = R.string.priority_medium,
                        recommendation = R.string.rec_ethics_medium
                    )

                    else -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_ethics_integrity,
                        priority = R.string.priority_maintain,
                        recommendation = R.string.rec_ethics_maintain
                    )
                }

                4 -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_policies_documentation,
                        priority = R.string.priority_high,
                        recommendation = R.string.rec_policies_high
                    )

                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_policies_documentation,
                        priority = R.string.priority_medium,
                        recommendation = R.string.rec_policies_medium
                    )

                    else -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_policies_documentation,
                        priority = R.string.priority_maintain,
                        recommendation = R.string.rec_policies_maintain
                    )
                }

                5 -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_third_party_management,
                        priority = R.string.priority_high,
                        recommendation = R.string.rec_third_party_high
                    )

                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_third_party_management,
                        priority = R.string.priority_medium,
                        recommendation = R.string.rec_third_party_medium
                    )

                    else -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_third_party_management,
                        priority = R.string.priority_maintain,
                        recommendation = R.string.rec_third_party_maintain
                    )
                }

                else -> NextStepRecommendation(
                    pillarTitle = R.string.pillar_governance,
                    priority = R.string.priority_review,
                    recommendation = R.string.rec_generic_review
                )
            }
        }

    return pillarResults.sortedBy { recommendation ->
        when (recommendation.priority) {
            R.string.priority_high -> 0
            R.string.priority_medium -> 1
            R.string.priority_maintain -> 2
            else -> 3
        }
    }
}