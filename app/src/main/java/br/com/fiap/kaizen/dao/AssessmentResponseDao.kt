package br.com.fiap.kaizen.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.kaizen.model.AssessmentResponse

@Dao
interface AssessmentResponseDao {

    @Insert
    fun save(response: AssessmentResponse): Long

    @Insert
    fun saveAll(responses: List<AssessmentResponse>)

    @Query("SELECT * FROM tbl_assessment_response")
    fun getAllResponses(): List<AssessmentResponse>

    @Query("DELETE FROM tbl_assessment_response")
    fun deleteAllResponses()

    @Query("SELECT * FROM tbl_assessment_response WHERE pillarId = :pillarId")
    fun getResponsesByPillar(pillarId: Int): List<AssessmentResponse>
}