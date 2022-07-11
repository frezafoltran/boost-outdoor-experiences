package com.foltran.feature_experience.menu.domain

import android.util.Log
import com.foltran.core_localstorage.internalstorage.images.getBitmapFromUrl
import com.foltran.core_map.images.utils.*
import com.foltran.core_networking.core.models.Resource
import com.foltran.core_utils.observable.ActionLiveData
import com.foltran.feature_experience.core.domain.ExperienceRepository
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.core_experience.shared.data.model.ExperienceHighlightSet
import com.foltran.feature_experience.menu.presentation.ImageToSave
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext


interface ExperienceMenuUseCase {
    val saveImageByUrlToInternalStorage: ActionLiveData<ImageToSave>

    fun getExperience(experienceId: String): Flow<Resource<Experience>>
    suspend fun syncBitmapsForExperience(
        experienceId: String,
        experienceHighlights: List<ExperienceHighlightSet>,
        experiencePolyline: String
    )
}

class ExperienceMenuUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: ExperienceRepository
) : ExperienceMenuUseCase {

    override val saveImageByUrlToInternalStorage = ActionLiveData<ImageToSave>()

    override fun getExperience(experienceId: String) = repository.getExperience(experienceId)

    private fun ExperienceHighlightSet.getCoordinatesForHighlightSet(
        coords: List<List<Double>>,
    ): List<List<List<Double>>> {

        val coordListForSet = mutableListOf<List<List<Double>>>()
        highlights.forEach {
            coordListForSet.add(coords.subList(it.startIndex, it.endIndex))
        }
        return coordListForSet
    }

    override suspend fun syncBitmapsForExperience(
        experienceId: String,
        experienceHighlights: List<ExperienceHighlightSet>,
        experiencePolyline: String
    ) {
        val numberOfOptions = experienceHighlights.size + 1 //accounts for option of no highlight

        val bitmaps = getStaticMapForHighlightsFromInternalStorage(
            experienceId = experienceId,
            highlightIds = experienceHighlights.map { it.experienceHighlightSetId }
        )

        if (bitmaps.size == numberOfOptions) {
            saveImageByUrlToInternalStorage.sendActionAsync(
                ImageToSave(
                    experienceId = experienceId,
                    bitmaps = bitmaps.toList(),
                    shouldSave = false
                )
            )
        } else {
            withContext(dispatcher) {
                repository.getExperienceStream(experienceId).onEach {
                    when (it) {
                        is Resource.Error -> {
                            Log.i("JVFF", it.message ?: "")
                        }
                        is Resource.Success -> {

                            val coords = it.data?.latLng ?: emptyList()
                            val baseBitMapUrl = getStaticMapUrlForHighlightBase(coords)
                            getBitmapFromUrl(baseBitMapUrl)?.let { bm ->
                                bitmaps.add(
                                    LapBitmap(
                                        bitmap = bm,
                                        lapId = "-1"
                                    )
                                )
                            }
                            experienceHighlights.forEach { highlightSet ->

                                val curBitMapUrl = getStaticMapUrlForHighlightSet(
                                    highlightSet.getCoordinatesForHighlightSet(coords),
                                    experiencePolyline
                                )
                                getBitmapFromUrl(curBitMapUrl)?.let { bm ->
                                    bitmaps.add(
                                        LapBitmap(
                                            bitmap = bm,
                                            lapId = highlightSet.experienceHighlightSetId
                                        )
                                    )
                                }
                            }

                            saveImageByUrlToInternalStorage.sendActionAsync(
                                ImageToSave(
                                    experienceId = experienceId,
                                    bitmaps = bitmaps.toList(),
                                    shouldSave = true
                                )
                            )

                        }
                    }
                }.launchIn(this)
            }
        }
    }
}