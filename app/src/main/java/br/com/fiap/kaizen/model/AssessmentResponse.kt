package br.com.fiap.kaizen.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_assessment_response")
data class AssessmentResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionId: Int,
    val pillarId: Int,
    val pillarTitle: String,
    val questionText: String,
    val answerScore: Int
)