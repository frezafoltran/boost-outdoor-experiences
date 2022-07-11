package com.foltran.core_experience.profile.repository

import androidx.paging.*
import com.foltran.core_experience.profile.model.ProfileFeedItem
import com.foltran.core_experience.room.db.ExperienceFeedDb
import com.foltran.core_experience.room.mediator.PageKeyedRemoteMediator
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.core_experience.shared.data.model.ExperienceItem
import com.foltran.core_experience.shared.data.remote.ExperienceService
import com.foltran.core_networking.core.models.Resource
import com.foltran.core_utils.date.formatUTCDateToYear
import com.foltran.core_utils.date.utcToYearNumber
import com.foltran.core_utils.distance.getDistanceBucket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

enum class ProfileFeedExperienceSortingOptions {
    Date,
    Distance,
    Location
}

interface ProfileFeedRepository {
    fun getExperiences(): Flow<Resource<List<ExperienceItem>>>
    fun getExperiences(
        pageSize: Int,
        sortingOptions: ProfileFeedExperienceSortingOptions
    ): Flow<PagingData<ProfileFeedItem>>
}

class ProfileFeedRepositoryImpl(val service: ExperienceService, val db: ExperienceFeedDb) :
    ProfileFeedRepository {

    override fun getExperiences(): Flow<Resource<List<ExperienceItem>>> = flow {
        try {
            emit(Resource.Loading())

            service.getExperiences().let { response ->
                if (response.isSuccessful && response.body() != null) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    emit(Resource.Error("An unexpected error occurred"))
                }
            }

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection and try again"))
        }
    }

    private fun insertSeparatorsForSorting(
        before: ProfileFeedItem.ExperienceListItem?,
        after: ProfileFeedItem.ExperienceListItem?,
        sortingOptions: ProfileFeedExperienceSortingOptions
    ): ProfileFeedItem? =
        if (after == null) {
            ProfileFeedItem.Footer
        } else
            when (sortingOptions) {
                ProfileFeedExperienceSortingOptions.Distance -> {
                    val beforeBucket = before?.item?.distance?.getDistanceBucket()
                    val afterBucket = after.item.distance.getDistanceBucket()
                    if (before == null || afterBucket < beforeBucket!!) {
                        ProfileFeedItem.Header("> $afterBucket km")
                    } else null
                }
                ProfileFeedExperienceSortingOptions.Location -> {
                    val beforeLocationCity = before?.item?.locationCity
                    val afterLocationCity = after.item.locationCity
                    if (before == null || afterLocationCity != beforeLocationCity!!) {
                        ProfileFeedItem.Header("$afterLocationCity")
                    } else null
                }
                else -> {
                    val afterYearParsed = after.item.startDate.formatUTCDateToYear()
                    val afterYear = after.item.startDate.utcToYearNumber()

                    val beforeYear = before?.item?.startDate?.utcToYearNumber()


                    if (before == null || afterYear < beforeYear!!) {
                        ProfileFeedItem.Header(afterYearParsed)
                    } else null
                }
            }


    @OptIn(ExperimentalPagingApi::class)
    override fun getExperiences(
        pageSize: Int,
        sortingOptions: ProfileFeedExperienceSortingOptions
    ): Flow<PagingData<ProfileFeedItem>> = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PageKeyedRemoteMediator(db, service)
    ) {
        when (sortingOptions) {
            ProfileFeedExperienceSortingOptions.Distance -> db.experiences()
                .allExperiencesSortedByDistance()
            ProfileFeedExperienceSortingOptions.Location -> db.experiences()
                .allExperiencesSortedByLocationCity()
            else -> db.experiences().allExperiences()
        }
    }.flow.map { pagingData ->
        pagingData.map { ProfileFeedItem.ExperienceListItem(it) }
    }.map {
        it.insertSeparators { before, after ->

            return@insertSeparators insertSeparatorsForSorting(before, after, sortingOptions)
        }
    }
}