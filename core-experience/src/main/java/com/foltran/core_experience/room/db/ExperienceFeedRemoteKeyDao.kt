package com.foltran.core_experience.room.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.foltran.core_experience.room.key.ExperienceFeedRemoteKey

@Dao
interface ExperienceFeedRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: ExperienceFeedRemoteKey)

    @Query("SELECT * FROM remote_keys WHERE id = '1'")
    suspend fun getAll(): ExperienceFeedRemoteKey

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAll()

//    @Query("SELECT * FROM remote_keys WHERE subreddit = :subreddit")
//    suspend fun remoteKeyByPost(subreddit: String): ExperienceFeedRemoteKey
//
//    @Query("DELETE FROM remote_keys WHERE subreddit = :subreddit")
//    suspend fun deleteBySubreddit(subreddit: String)
}