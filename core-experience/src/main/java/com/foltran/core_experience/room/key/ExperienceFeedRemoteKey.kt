package com.foltran.core_experience.room.key

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class ExperienceFeedRemoteKey(
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val id: String,
    val nextPageKey: String?
)