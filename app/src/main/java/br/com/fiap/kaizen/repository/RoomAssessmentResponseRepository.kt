package br.com.fiap.kaizen.repository

import android.content.Context
import br.com.fiap.kaizen.dao.RecipeDatabase
import br.com.fiap.kaizen.model.AssessmentResponse

class RoomAssessmentResponseRepository(context: Context) : AssessmentResponseRepository {

    private val db = RecipeDatabase.getDatabase(context)

    override fun saveAllResponses(responses: List<AssessmentResponse>) {
        db.assessmentResponseDao().saveAll(responses)
    }

    override fun getAllResponses(): List<AssessmentResponse> {
        return db.assessmentResponseDao().getAllResponses()
    }

    override fun deleteAllResponses() {
        db.assessmentResponseDao().deleteAllResponses()
    }
}