package br.com.fiap.recipes.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.recipes.model.User

@Dao
interface UserDao {

    @Insert
    fun save(user: User)

    @Delete
    fun delete(user: User): Int

    @Update
    fun update(user: User): Int

    @Query("SELECT * FROM tb_user WHERE id = :id LIMIT 1")
    fun getUserById(id: Int): User

    @Query("SELECT * FROM tb_user WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM tb_user WHERE " +
            "email = :email AND password = :password LIMIT 1")
    fun login(email: String, password: String): User?

}