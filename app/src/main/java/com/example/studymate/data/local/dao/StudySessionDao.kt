package com.example.studymate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.studymate.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity): Long

    @Query("SELECT * FROM study_sessions ORDER BY createdAt DESC")
    fun getAllSessions(): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE materiId = :materiId ORDER BY createdAt DESC")
    fun getSessionsByMateri(materiId: Int): Flow<List<StudySessionEntity>>

    @Query("SELECT SUM(durationMs) FROM study_sessions WHERE materiId = :materiId")
    suspend fun getTotalDurationForMateri(materiId: Int): Long?
}
