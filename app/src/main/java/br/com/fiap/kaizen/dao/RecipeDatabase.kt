package br.com.fiap.kaizen.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.kaizen.model.User
import br.com.fiap.kaizen.model.Company

@Database(
    entities = [
        User::class,
        Company::class
    ],
    version = 3
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun companyDao(): CompanyDao

    companion object {
        private lateinit var instance: RecipeDatabase

        fun getDatabase(context: Context): RecipeDatabase {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    context,
                    RecipeDatabase::class.java,
                    "recipes_db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}