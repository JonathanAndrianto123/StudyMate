package com.example.studymate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.studymate.data.local.entity.MateriEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MateriDao {

    @Query("SELECT * FROM materi")
    fun getAllMateri(): Flow<List<MateriEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMateri(materi: MateriEntity)

    @Query("SELECT * FROM materi WHERE id = :id LIMIT 1")
    fun getMateriById(id: Int): Flow<MateriEntity?>
}
