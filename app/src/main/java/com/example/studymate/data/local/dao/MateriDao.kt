package com.example.studymate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.studymate.data.local.entity.MateriEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MateriDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMateri(materi: MateriEntity): Long

    @Query("SELECT * FROM materi WHERE userId = :userId")
    fun getAllMateri(userId: Int): Flow<List<MateriEntity>>

    @Query("SELECT * FROM materi WHERE id = :id")
    fun getMateriById(id: Int): Flow<MateriEntity>
}
