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


private val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Add location columns to study_sessions
        db.execSQL("ALTER TABLE study_sessions ADD COLUMN latitude REAL DEFAULT NULL")
        db.execSQL("ALTER TABLE study_sessions ADD COLUMN longitude REAL DEFAULT NULL")
    }
}


private val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE study_sessions ADD COLUMN distractionCount INTEGER NOT NULL DEFAULT 0")
    }
}

private val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Method 1: Check existing columns
        val cursor = db.query("PRAGMA table_info(study_sessions)")
        val existingColumns = mutableSetOf<String>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            existingColumns.add(name)
        }
        cursor.close()

        // Method 2: Defensive Try-Catch for column additions
        // Even if the check above says it's missing, some weird state might cause an error.
        // We catch the specific error for duplicate column.
        
        if (!existingColumns.contains("userId")) {
            try {
                db.execSQL("ALTER TABLE study_sessions ADD COLUMN userId INTEGER NOT NULL DEFAULT 0")
            } catch (e: Exception) {
                // If it fails (likely duplicate column), we just ignore it as the column exists.
                e.printStackTrace()
            }
        }
        
        if (!existingColumns.contains("durationMs")) {
            try {
                db.execSQL("ALTER TABLE study_sessions ADD COLUMN durationMs INTEGER NOT NULL DEFAULT 0")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // Index creation usually has IF NOT EXISTS, but try-catch is safer too 
        try {
            db.execSQL("CREATE INDEX IF NOT EXISTS index_study_sessions_materiId ON study_sessions(materiId)")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Database(
    entities = [MateriEntity::class, UserEntity::class, StudySessionEntity::class],
    version = 7,
    exportSchema = false
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
