package com.foltran.core_experience.room.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.foltran.core_experience.room.db.ExperienceFeedDao
import com.foltran.core_experience.room.db.ExperienceFeedDb
import com.foltran.core_experience.room.db.ExperienceFeedRemoteKeyDao
import com.foltran.core_experience.room.key.ExperienceFeedRemoteKey
import com.foltran.core_experience.shared.data.model.ExperienceItem
import com.foltran.core_experience.shared.data.remote.ExperienceService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val db: ExperienceFeedDb,
    private val redditApi: ExperienceService
) : RemoteMediator<Int, ExperienceItem>() {
    private val postDao: ExperienceFeedDao = db.experiences()
    private val remoteKeyDao: ExperienceFeedRemoteKeyDao = db.remoteKeys()

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ExperienceItem>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                REFRESH -> null
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {

                    val remoteKey = db.withTransaction {
                        remoteKeyDao.getAll()
                    }

                    if (remoteKey.nextPageKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nextPageKey
                }
            }

            Log.i("JVFF", "calling API")
            val data = redditApi.getExperiences().body()

            db.withTransaction {
                if (loadType == REFRESH) {
                    postDao.deleteAll()
                    remoteKeyDao.deleteAll()
                }

                remoteKeyDao.insert(ExperienceFeedRemoteKey("1", null))
                postDao.insertAll(data.orEmpty())
            }

            return MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}
