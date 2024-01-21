package com.example.practice.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_credentials")
data class UserCredentialsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val password: String
)