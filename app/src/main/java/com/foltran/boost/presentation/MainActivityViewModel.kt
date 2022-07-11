package com.foltran.boost.presentation

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foltran.boost.R
import com.foltran.core_ui.components.ToolbarComponentParams
import com.foltran.feature_experience.feed.presentation.getDefaultToolbar
import com.foltran.feature_experience.feed.presentation.getExperienceFeedToolbar
import com.foltran.feature_experience.record.presentation.getExperienceStartRecordToolbar
import com.foltran.feature_profile.core.presentation.getProfileFeedToolbar

enum class BottomNavigationBarRoutes {
    ExperienceFeed { override val id = R.id.home },
    ExperienceStartRecord { override val id = R.id.experience_record },
    Profile { override val id = R.id.profile };
    abstract val id: Int
}
sealed class MainViewState{
    data class ShowExperienceFeed(val menuId: Int = R.id.home): MainViewState()
    data class ShowProfile(val menuId: Int = R.id.profile): MainViewState()
    data class ShowExperienceStartRecord(val menuId: Int = R.id.experience_record): MainViewState()
    data class SwitchToolbars(val route: BottomNavigationBarRoutes): MainViewState()
}

class MainActivityViewModel: ViewModel() {

    private val _state = MutableLiveData<MainViewState>()
    val state: LiveData<MainViewState> = _state

    private val _prevToolbar = MutableLiveData<ToolbarComponentParams>()
    val prevToolbar: LiveData<ToolbarComponentParams> = _prevToolbar

    private val searchEditText = MutableLiveData("ihi")

    val experienceFeedToolbar = getExperienceFeedToolbar(searchEditText){
        setCurToolbar(experienceFeedToolbarDefault)
    }

    val experienceFeedToolbarDefault = getDefaultToolbar(
        onClickProfile = { onClickProfile() },
        onClickSearch = { onClickSearch() }
    )

    val profileToolbar = getProfileFeedToolbar()

    val experienceRecordToolbar = getExperienceStartRecordToolbar()

    private val _curToolbar = MutableLiveData(experienceFeedToolbarDefault)
    val curToolbar: LiveData<ToolbarComponentParams> = _curToolbar

    fun setCurToolbar(toolbar: ToolbarComponentParams? = null){

        _prevToolbar.value = _curToolbar.value
        _curToolbar.value = toolbar
    }

    fun onClickProfile(){
        _state.postValue(MainViewState.ShowProfile())
    }

    fun onClickSearch(){
        _state.postValue(MainViewState.SwitchToolbars(BottomNavigationBarRoutes.ExperienceFeed))
    }

    fun setOnItemSelectedListenerBottomNavigation(item: MenuItem): Boolean {
        return when(item.itemId) {
            BottomNavigationBarRoutes.ExperienceFeed.id -> {
                setCurToolbar(experienceFeedToolbarDefault)
                _state.postValue(MainViewState.ShowExperienceFeed())
                true
            }
            BottomNavigationBarRoutes.ExperienceStartRecord.id-> {
                setCurToolbar(experienceRecordToolbar)
                _state.postValue(MainViewState.ShowExperienceStartRecord())
                true
            }
            BottomNavigationBarRoutes.Profile.id -> {
                setCurToolbar(profileToolbar)
                _state.postValue(MainViewState.ShowProfile())
                true
            }
            else -> false
        }
    }
}