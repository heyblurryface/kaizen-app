package br.com.fiap.kaizen.repository

import br.com.fiap.kaizen.model.Company

interface CompanyRepository {
    fun saveCompany(company: Company): Long
    fun updateCompany(company: Company): Int
    fun getLastCompany(): Company?
    fun getCompanyById(id: Long): Company?
}