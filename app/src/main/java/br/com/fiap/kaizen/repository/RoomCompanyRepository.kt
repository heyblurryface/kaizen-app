package br.com.fiap.kaizen.repository

import android.content.Context
import androidx.room.Room
import br.com.fiap.kaizen.dao.KaizenDatabase
import br.com.fiap.kaizen.model.Company

class RoomCompanyRepository(context: Context) : CompanyRepository {

    private val db = Room.databaseBuilder(
        context,
        KaizenDatabase::class.java,
        "db_kaizen"
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    override fun saveCompany(company: Company): Long {
        return db.companyDao().save(company)
    }

    override fun updateCompany(company: Company): Int {
        return db.companyDao().update(company)
    }

    override fun getLastCompany(): Company? {
        return db.companyDao().getLastCompany()
    }

    override fun getCompanyById(id: Long): Company? {
        return db.companyDao().getCompanyById(id)
    }
}