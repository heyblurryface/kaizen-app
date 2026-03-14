package br.com.fiap.kaizen.repository

import br.com.fiap.kaizen.model.User

interface UserRepository {

    fun saveUser(user: User)
    fun getUser(id: Int = 0): User
    fun getUserByEmail(email: String): User?
    fun login(email: String, password: String): Boolean
    fun updateUser(user: User): Int
    fun deleteUser(user: User)

}