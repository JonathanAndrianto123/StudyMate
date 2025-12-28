package com.example.studymate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materi")
data class MateriEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int = 0, // Scoping to user
    val name: String,
    val description: String = "",
    val targetTime: String,
    val progress: Int = 0,
    val todaySeconds: Int = 0,
    val totalSeconds: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
