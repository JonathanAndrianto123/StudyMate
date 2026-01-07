package com.example.studymate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materi")
data class MateriEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val name: String,
    val description: String,
    val targetTime: String // storing as String "HH:mm:ss" or just raw string for now
)
