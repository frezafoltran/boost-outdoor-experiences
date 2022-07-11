package com.foltran.core_experience.feed.repository

import androidx.paging.*
import com.foltran.core_experience.shared.data.remote.ExperienceService
import com.foltran.core_experience.feed.presentation.model.ExperienceFeedItem
import com.foltran.core_experience.room.db.ExperienceFeedDb
import com.foltran.core_experience.room.mediator.PageKeyedRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ExperienceFeedRepository {
    fun getExperiences(pageSize: Int): Flow<PagingData<ExperienceFeedItem>>
}

class ExperienceFeedRepositoryImpl(val service: ExperienceService, val db: ExperienceFeedDb) :
    ExperienceFeedRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getExperiences(pageSize: Int) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PageKeyedRemoteMediator(db, service)
    ) {
        db.experiences().allExperiences()
    }.flow.map {
        pagingData -> pagingData.map { ExperienceFeedItem.ExperienceListItem(it) }
    }.map {
        it.insertSeparators { before, after ->
            if (after == null) {
                return@insertSeparators ExperienceFeedItem.LastItem
            }

            if (before == null) {
                return@insertSeparators ExperienceFeedItem.FirstItem
            }
            else null
        }
    }
}