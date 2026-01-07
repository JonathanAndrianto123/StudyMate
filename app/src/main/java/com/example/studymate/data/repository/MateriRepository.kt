package com.example.studymate.data.repository

import com.example.studymate.data.local.dao.MateriDao
import com.example.studymate.data.local.entity.MateriEntity
import kotlinx.coroutines.flow.Flow

class MateriRepository(
    private val dao: MateriDao
) {
    fun getAllMateri(userId: Int): Flow<List<MateriEntity>> {
        return dao.getAllMateri(userId)
    }

    suspend fun addMateri(materi: MateriEntity) {
        dao.insertMateri(materi)
    }

    fun getMateriById(id: Int): Flow<MateriEntity?> {
        return dao.getMateriById(id)
    }

    suspend fun updateMateriProgress(materiId: Int, newProgressPercentage: Int) {
        val materi = dao.getMateriByIdOnce(materiId)
        materi?.let {
            val updated = it.copy(progress = newProgressPercentage)
            dao.updateMateri(updated)
        }
    }

    suspend fun updateMateriTotalSeconds(materiId: Int, durationMs: Long) {
        val materi = dao.getMateriByIdOnce(materiId)
        materi?.let {
            val additionalSeconds = (durationMs / 1000).toInt()
            val updated = it.copy(
                totalSeconds = it.totalSeconds + additionalSeconds,
                todaySeconds = it.todaySeconds + additionalSeconds
            )
            dao.updateMateri(updated)
        }
    }
}
