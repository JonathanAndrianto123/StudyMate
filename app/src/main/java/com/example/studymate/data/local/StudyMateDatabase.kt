package com.example.studymate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studymate.data.local.dao.MateriDao
import com.example.studymate.data.local.entity.MateriEntity
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE materi ADD COLUMN description TEXT NOT NULL DEFAULT ''"
        )
        db.execSQL(
            "ALTER TABLE materi ADD COLUMN todayTime TEXT NOT NULL DEFAULT '0 hr 00 min'"
        )
        db.execSQL(
            "ALTER TABLE materi ADD COLUMN totalTime TEXT NOT NULL DEFAULT '0 hr 00 min'"
        )
    }
}

@Database(
    entities = [MateriEntity::class],
    version = 2
)
abstract class StudymateDatabase : RoomDatabase() {

    abstract fun materiDao(): MateriDao

    companion object {
        @Volatile
        private var INSTANCE: StudymateDatabase? = null

        fun getDatabase(context: Context): StudymateDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    StudymateDatabase::class.java,
                    "studymate_db"
                ).addMigrations(MIGRATION_1_2).build().also { INSTANCE = it }
            }
        }
    }
}
