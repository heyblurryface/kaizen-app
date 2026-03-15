package br.com.fiap.kaizen.repository

import br.com.fiap.kaizen.model.AssessmentResponse

interface AssessmentResponseRepository {
    fun saveAllResponses(responses: List<AssessmentResponse>)
    fun getAllResponses(): List<AssessmentResponse>
    fun deleteAllResponses()
}