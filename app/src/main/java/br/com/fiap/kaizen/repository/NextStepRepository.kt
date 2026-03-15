package br.com.fiap.kaizen.repository

import br.com.fiap.kaizen.model.AssessmentResponse
import br.com.fiap.kaizen.model.NextStepRecommendation

fun getNextStepRecommendations(
    responses: List<AssessmentResponse>
): List<NextStepRecommendation> {
    if (responses.isEmpty()) return emptyList()

    val pillarResults = responses
        .groupBy { it.pillarTitle }
        .map { (pillarTitle, pillarResponses) ->
            val score = pillarResponses.sumOf { it.answerScore }
            val maxScore = pillarResponses.size * 3
            val percentage = if (maxScore > 0) score.toFloat() / maxScore else 0f

            val recommendation = when (pillarTitle) {
                "Governance" -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle,
                        "High priority",
                        "Define clear owners, roles, and governance routines for critical decisions, risks, and compliance topics."
                    )
                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle,
                        "Medium priority",
                        "Strengthen governance routines by formalizing accountability and improving periodic monitoring of risks and controls."
                    )
                    else -> NextStepRecommendation(
                        pillarTitle,
                        "Maintain",
                        "Maintain current governance practices and continue evolving formal oversight and decision-making routines."
                    )
                }

                "Information Security" -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle,
                        "High priority",
                        "Implement basic access control, backup routines, and employee guidance on passwords, suspicious emails, and secure handling of files."
                    )
                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle,
                        "Medium priority",
                        "Improve security controls by reviewing access lifecycle, backup coverage, and incident handling practices."
                    )
                    else -> NextStepRecommendation(
                        pillarTitle,
                        "Maintain",
                        "Maintain the current security baseline and continue strengthening preventive and monitoring controls."
                    )
                }

                "Ethics and Integrity" -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle,
                        "High priority",
                        "Establish basic ethics guidelines, reporting channels, and awareness actions related to conduct and conflicts of interest."
                    )
                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle,
                        "Medium priority",
                        "Reinforce ethics communication, consistency in rule enforcement, and leadership role-modeling."
                    )
                    else -> NextStepRecommendation(
                        pillarTitle,
                        "Maintain",
                        "Maintain the integrity framework and continue reinforcing ethical behavior across teams."
                    )
                }

                "Policies and Documentation" -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle,
                        "High priority",
                        "Document critical routines, define minimum policies, and organize key documents so they are accessible and version-controlled."
                    )
                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle,
                        "Medium priority",
                        "Improve documentation review cycles, accessibility, and version control for relevant documents."
                    )
                    else -> NextStepRecommendation(
                        pillarTitle,
                        "Maintain",
                        "Maintain documentation practices and continue improving consistency and governance over updates."
                    )
                }

                "Third-Party Management and Awareness" -> when {
                    percentage < 0.40f -> NextStepRecommendation(
                        pillarTitle,
                        "High priority",
                        "Create minimum third-party evaluation criteria and reinforce awareness initiatives on security, ethics, and compliance."
                    )
                    percentage < 0.70f -> NextStepRecommendation(
                        pillarTitle,
                        "Medium priority",
                        "Strengthen third-party oversight and increase awareness actions for employees and relevant external parties."
                    )
                    else -> NextStepRecommendation(
                        pillarTitle,
                        "Maintain",
                        "Maintain the current awareness and third-party management baseline while improving monitoring maturity."
                    )
                }

                else -> NextStepRecommendation(
                    pillarTitle,
                    "Review",
                    "Review this pillar and define practical improvement actions based on current assessment results."
                )
            }

            recommendation
        }

    return pillarResults.sortedBy { recommendation ->
        when (recommendation.priority) {
            "High priority" -> 0
            "Medium priority" -> 1
            "Maintain" -> 2
            else -> 3
        }
    }
}