package com.example.studymate.data.repository

import com.example.studymate.data.local.dao.MateriDao
import com.example.studymate.data.local.entity.MateriEntity
import kotlinx.coroutines.flow.Flow

class MateriRepository(
    private val dao: MateriDao
) {
    fun getAllMateri(): Flow<List<MateriEntity>> =
        dao.getAllMateri()

    suspend fun addMateri(materi: MateriEntity) {
        dao.insertMateri(materi)
    }

    fun getMateriById(id: Int): Flow<MateriEntity?> {
        return dao.getMateriById(id)
    }
}
