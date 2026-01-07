package com.example.studymate.data.repository

import com.example.studymate.data.local.dao.MateriDao
import com.example.studymate.data.local.entity.MateriEntity
import kotlinx.coroutines.flow.Flow

class MateriRepository(private val dao: MateriDao) {
    suspend fun insertMateri(materi: MateriEntity): Long = dao.insertMateri(materi)
    
    fun getAllMateri(userId: Int): Flow<List<MateriEntity>> = dao.getAllMateri(userId)
    
    fun getMateriById(id: Int): Flow<MateriEntity> = dao.getMateriById(id)
}
