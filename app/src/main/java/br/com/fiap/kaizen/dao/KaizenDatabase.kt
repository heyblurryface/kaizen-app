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
abstract class KaizenDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun companyDao(): CompanyDao
    abstract fun assessmentResponseDao(): AssessmentResponseDao

    companion object {
        private lateinit var instance: KaizenDatabase

        fun getDatabase(context: Context): KaizenDatabase {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    context,
                    KaizenDatabase::class.java,
                    "kaizen_db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}