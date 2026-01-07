package com.example.studymate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studymate.data.local.dao.MateriDao
import com.example.studymate.data.local.dao.StudySessionDao
import com.example.studymate.data.local.dao.UserDao
import com.example.studymate.data.local.entity.MateriEntity
import com.example.studymate.data.local.entity.StudySessionEntity
import com.example.studymate.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, StudySessionEntity::class, MateriEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StudymateDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun materiDao(): MateriDao

    companion object {
        @Volatile
        private var INSTANCE: StudymateDatabase? = null

        fun getDatabase(context: Context): StudymateDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudymateDatabase::class.java,
                    "studymate_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
