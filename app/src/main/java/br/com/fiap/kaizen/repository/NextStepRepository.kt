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
                        recommendation = R.string.rec_governance_high,
                        recommendationDetail = R.string.rec_governance_high_detail
                    )

                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_governance,
                        priority = R.string.priority_medium,
                        recommendation = R.string.rec_governance_medium,
                        recommendationDetail = R.string.rec_governance_medium_detail
                    )

                    else -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_governance,
                        priority = R.string.priority_maintain,
                        recommendation = R.string.rec_governance_maintain,
                        recommendationDetail = R.string.rec_governance_maintain_detail
                    )
                }

                2 -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_information_security,
                        priority = R.string.priority_high,
                        recommendation = R.string.rec_security_high,
                        recommendationDetail = R.string.rec_security_high_detail
                    )

                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_information_security,
                        priority = R.string.priority_medium,
                        recommendation = R.string.rec_security_medium,
                        recommendationDetail = R.string.rec_security_medium_detail
                    )

                    else -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_information_security,
                        priority = R.string.priority_maintain,
                        recommendation = R.string.rec_security_maintain,
                        recommendationDetail = R.string.rec_security_maintain_detail
                    )
                }

                3 -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_ethics_integrity,
                        priority = R.string.priority_high,
                        recommendation = R.string.rec_ethics_high,
                        recommendationDetail = R.string.rec_ethics_high_detail
                    )

                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_ethics_integrity,
                        priority = R.string.priority_medium,
                        recommendation = R.string.rec_ethics_medium,
                        recommendationDetail = R.string.rec_ethics_medium_detail
                    )

                    else -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_ethics_integrity,
                        priority = R.string.priority_maintain,
                        recommendation = R.string.rec_ethics_maintain,
                        recommendationDetail = R.string.rec_ethics_maintain_detail
                    )
                }

                4 -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_policies_documentation,
                        priority = R.string.priority_high,
                        recommendation = R.string.rec_policies_high,
                        recommendationDetail = R.string.rec_policies_high_detail
                    )

                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_policies_documentation,
                        priority = R.string.priority_medium,
                        recommendation = R.string.rec_policies_medium,
                        recommendationDetail = R.string.rec_policies_medium_detail
                    )

                    else -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_policies_documentation,
                        priority = R.string.priority_maintain,
                        recommendation = R.string.rec_policies_maintain,
                        recommendationDetail = R.string.rec_policies_maintain_detail
                    )
                }

                5 -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_third_party_management,
                        priority = R.string.priority_high,
                        recommendation = R.string.rec_third_party_high,
                        recommendationDetail = R.string.rec_third_party_high_detail
                    )

                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_third_party_management,
                        priority = R.string.priority_medium,
                        recommendation = R.string.rec_third_party_medium,
                        recommendationDetail = R.string.rec_third_party_medium_detail
                    )

                    else -> NextStepRecommendation(
                        pillarTitle = R.string.pillar_third_party_management,
                        priority = R.string.priority_maintain,
                        recommendation = R.string.rec_third_party_maintain,
                        recommendationDetail = R.string.rec_third_party_maintain_detail
                    )
                }

                else -> NextStepRecommendation(
                    pillarTitle = R.string.pillar_governance,
                    priority = R.string.priority_review,
                    recommendation = R.string.rec_generic_review,
                    recommendationDetail = R.string.rec_generic_review_detail
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
