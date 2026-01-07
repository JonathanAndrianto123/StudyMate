package com.example.studymate.data.repository

import com.example.studymate.data.local.dao.StudySessionDao
import com.example.studymate.data.local.entity.StudySessionEntity
import kotlinx.coroutines.flow.Flow

class StudySessionRepository(
    private val dao: StudySessionDao
) {
    suspend fun saveSession(session: StudySessionEntity): Long {
        return dao.insertSession(session)
    }

    fun getAllSessions(): Flow<List<StudySessionEntity>> {
        return dao.getAllSessions()
    }

    fun getSessionsByMateri(materiId: Int): Flow<List<StudySessionEntity>> {
        return dao.getSessionsByMateri(materiId)
    }

    suspend fun getTotalDurationForMateri(materiId: Int): Long {
        return dao.getTotalDurationForMateri(materiId) ?: 0L
    }
}
