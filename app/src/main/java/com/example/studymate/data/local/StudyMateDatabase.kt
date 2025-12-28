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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Check if columns exist before adding them
        val cursor = db.query("PRAGMA table_info(materi)")
        val existingColumns = mutableSetOf<String>()
        
        while (cursor.moveToNext()) {
            val columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            existingColumns.add(columnName)
        }
        cursor.close()
        
        // Only add columns if they don't exist
        if (!existingColumns.contains("description")) {
            db.execSQL(
                "ALTER TABLE materi ADD COLUMN description TEXT NOT NULL DEFAULT ''"
            )
        }
        if (!existingColumns.contains("todayTime")) {
            db.execSQL(
                "ALTER TABLE materi ADD COLUMN todayTime TEXT NOT NULL DEFAULT '0 hr 00 min'"
            )
        }
        if (!existingColumns.contains("totalTime")) {
            db.execSQL(
                "ALTER TABLE materi ADD COLUMN totalTime TEXT NOT NULL DEFAULT '0 hr 00 min'"
            )
        }
    }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create users table
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                email TEXT NOT NULL,
                password TEXT NOT NULL,
                createdAt INTEGER NOT NULL
            )
            """.trimIndent()
        )

        // Create study_sessions table
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS study_sessions (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                materiId INTEGER NOT NULL,
                materiName TEXT NOT NULL,
                startTime INTEGER NOT NULL,
                endTime INTEGER NOT NULL,
                durationMs INTEGER NOT NULL,
                createdAt INTEGER NOT NULL,
                FOREIGN KEY(materiId) REFERENCES materi(id) ON DELETE CASCADE
            )
            """.trimIndent()
        )
    }
}

@Database(
    entities = [MateriEntity::class, UserEntity::class, StudySessionEntity::class],
    version = 4
)
abstract class StudymateDatabase : RoomDatabase() {

    abstract fun materiDao(): MateriDao
    abstract fun userDao(): UserDao
    abstract fun studySessionDao(): StudySessionDao

    companion object {
        @Volatile
        private var INSTANCE: StudymateDatabase? = null

        fun getDatabase(context: Context): StudymateDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    StudymateDatabase::class.java,
                    "studymate_db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
