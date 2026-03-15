package br.com.fiap.kaizen.repository

import br.com.fiap.kaizen.model.AssessmentPillar
import br.com.fiap.kaizen.model.AssessmentQuestion

fun getAssessmentPillars(): List<AssessmentPillar> {
    return listOf(
        AssessmentPillar(
            id = 1,
            title = "Governance",
            questions = listOf(
                AssessmentQuestion(1, "Does the company have defined owners for risk, compliance, or security topics?"),
                AssessmentQuestion(2, "Does senior leadership actively support governance and compliance practices?"),
                AssessmentQuestion(3, "Are roles and responsibilities formally defined for critical processes?"),
                AssessmentQuestion(4, "Does the company periodically monitor risks or internal controls?"),
                AssessmentQuestion(5, "Are there meetings, committees, or forums to address strategic, risk, or compliance topics?"),
                AssessmentQuestion(6, "Do important company decisions follow at least minimally standardized criteria or processes?")
            )
        ),
        AssessmentPillar(
            id = 2,
            title = "Information Security",
            questions = listOf(
                AssessmentQuestion(7, "Does the company have access controls for important systems and information?"),
                AssessmentQuestion(8, "Do users have individual credentials to access corporate systems?"),
                AssessmentQuestion(9, "Does the company perform backups of critical data?"),
                AssessmentQuestion(10, "Is there a process to handle security incidents or information loss?"),
                AssessmentQuestion(11, "Are access rights of terminated employees removed or reviewed?"),
                AssessmentQuestion(12, "Are employees guided on handling suspicious emails, passwords, and files?"),
                AssessmentQuestion(13, "Is there basic protection for devices and systems, such as antivirus, updates, or monitoring?")
            )
        ),
        AssessmentPillar(
            id = 3,
            title = "Ethics and Integrity",
            questions = listOf(
                AssessmentQuestion(14, "Does the company have ethical conduct guidelines for employees?"),
                AssessmentQuestion(15, "Do employees receive guidance on ethical behavior and conflict of interest?"),
                AssessmentQuestion(16, "Is there any channel or mechanism to report irregularities, even informally?"),
                AssessmentQuestion(17, "Does the company handle rule violations consistently?"),
                AssessmentQuestion(18, "Is there concern regarding fraud prevention, undue favoritism, or conflicts of interest?"),
                AssessmentQuestion(19, "Does leadership act as a role model regarding expected conduct?")
            )
        ),
        AssessmentPillar(
            id = 4,
            title = "Policies and Documentation",
            questions = listOf(
                AssessmentQuestion(20, "Does the company have documented policies, standards, or procedures for key activities?"),
                AssessmentQuestion(21, "Are internal documents periodically reviewed or updated?"),
                AssessmentQuestion(22, "Do employees know where to find internal rules, procedures, or guidance?"),
                AssessmentQuestion(23, "Are key routines and critical processes formally documented?"),
                AssessmentQuestion(24, "Does the company keep important documents organized and accessible for consultation?"),
                AssessmentQuestion(25, "Is there at least minimal control over versions or updates of relevant documents?")
            )
        ),
        AssessmentPillar(
            id = 5,
            title = "Third-Party Management",
            questions = listOf(
                AssessmentQuestion(26, "Does the company assess risks or impacts before engaging third parties for key activities?"),
                AssessmentQuestion(27, "Are there minimum criteria to select critical suppliers or partners?"),
                AssessmentQuestion(28, "Does the company monitor third parties that access relevant information, systems, or processes?"),
                AssessmentQuestion(29, "Do third parties receive basic guidance on rules, security, or conduct when necessary?"),
                AssessmentQuestion(30, "Does the company promote awareness of security, ethics, or compliance in daily operations?"),
                AssessmentQuestion(31, "Is there internal communication reinforcing responsibilities and controls in critical processes?")
            )
        )
    )
}