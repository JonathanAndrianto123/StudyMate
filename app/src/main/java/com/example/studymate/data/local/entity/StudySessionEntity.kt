package com.example.studymate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "study_sessions",
    foreignKeys = [
        ForeignKey(
            entity = MateriEntity::class,
            parentColumns = ["id"],
            childColumns = ["materiId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index(value = ["materiId"])]
)
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int = 0, // Scoping to user
    val materiId: Int,
    val materiName: String,
    val startTime: Long,
    val endTime: Long,
    val durationMs: Long,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val distractionCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
