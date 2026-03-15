package br.com.fiap.kaizen.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.kaizen.model.User
import br.com.fiap.kaizen.model.Company
import br.com.fiap.kaizen.model.AssessmentResponse


@Database(
    entities = [
        User::class,
        Company::class,
        AssessmentResponse::class
    ],
    version = 4
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun companyDao(): CompanyDao

    abstract fun assessmentResponseDao(): AssessmentResponseDao
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