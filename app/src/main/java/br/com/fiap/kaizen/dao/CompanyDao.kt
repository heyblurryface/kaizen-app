package br.com.fiap.kaizen.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.kaizen.model.Company

@Dao
interface CompanyDao {

    @Insert
    fun save(company: Company): Long

    @Update
    fun update(company: Company): Int

    @Query("SELECT * FROM tbl_company WHERE id = :id")
    fun getCompanyById(id: Long): Company?

    @Query("SELECT * FROM tbl_company ORDER BY id DESC LIMIT 1")
    fun getLastCompany(): Company?
}