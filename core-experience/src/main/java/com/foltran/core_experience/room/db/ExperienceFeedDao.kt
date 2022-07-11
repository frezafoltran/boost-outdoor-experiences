package com.foltran.core_experience.room.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.foltran.core_experience.shared.data.model.ExperienceItem

@Dao
interface ExperienceFeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<ExperienceItem>)

    @Query("SELECT * FROM experience_feed_items WHERE id > 1 ORDER BY startDate DESC")
    fun allExperiences(): PagingSource<Int, ExperienceItem>

    @Query("SELECT * FROM experience_feed_items WHERE id > 1 ORDER BY distance DESC")
    fun allExperiencesSortedByDistance(): PagingSource<Int, ExperienceItem>

    @Query("SELECT * FROM experience_feed_items WHERE id > 1 ORDER BY locationCity ASC")
    fun allExperiencesSortedByLocationCity(): PagingSource<Int, ExperienceItem>

    @Query("DELETE FROM experience_feed_items")
    suspend fun deleteAll()

//    @Query("SELECT * FROM posts WHERE subreddit = :subreddit ORDER BY indexInResponse ASC")
//    fun postsBySubreddit(subreddit: String): PagingSource<Int, ExperienceFeedItem>
//
//    @Query("DELETE FROM posts WHERE subreddit = :subreddit")
//    suspend fun deleteBySubreddit(subreddit: String)
//
//    @Query("SELECT MAX(indexInResponse) + 1 FROM posts WHERE subreddit = :subreddit")
//    suspend fun getNextIndexInSubreddit(subreddit: String): Int
}