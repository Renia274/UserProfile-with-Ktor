package com.example.practice.room



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserCredentialsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCredentials(credentials: UserCredentialsEntity)

    @Query("SELECT * FROM user_credentials WHERE username = :username AND password = :password")
    suspend fun getUserCredentials(username: String, password: String): UserCredentialsEntity?
}
