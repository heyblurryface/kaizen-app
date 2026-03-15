package br.com.fiap.kaizen.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_company")
data class Company(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val companyName: String,
    val respondentName: String,
    val role: String,
    val employees: Int,
    val sector: String,
    val size: String,
    val businessAreas: String,
    val hasThirdParties: String,
    val companyImage: ByteArray?
)