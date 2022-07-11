package com.foltran.boost.presentation

import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.foltran.boost.R
import com.foltran.boost.databinding.ActivityMainBinding
import com.foltran.core_ui.components.ToolbarComponentParams
import com.foltran.core_ui.databinding.ComponentToolbarBinding
import com.foltran.core_utils.extensions.fadeAnimation
import com.foltran.core_utils.extensions.replace
import com.foltran.feature_experience.feed.presentation.AppBarHeightInterface
import com.foltran.feature_experience.feed.presentation.ExperienceFeedFragment
import com.foltran.feature_experience.feed.presentation.listener.RecyclerViewListenerController
import com.foltran.feature_experience.record.presentation.ExperienceStartRecordFragment
import com.foltran.feature_profile.core.presentation.ProfileFragment
import com.google.android.material.navigation.NavigationBarView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity(), RecyclerViewListenerController, AppBarHeightInterface {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    private lateinit var toolbarBinding: ComponentToolbarBinding

    private var experienceFeedCurYScroll: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.stub.viewStub?.inflate()
        setContentView(binding.root)

        toolbarBinding = (binding.stub.binding as ComponentToolbarBinding)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.bg_primary)
        }

        openMainContainerFragment(ExperienceFeedFragment())
        setupBottomNavigationBar()
        setupObservers()
    }

    private fun openMainContainerFragment(fragment: Fragment, menuId: Int? = null){
        binding.appBarLayout.y = 0f
        experienceFeedCurYScroll = 0
        supportFragmentManager.replace(
            id = R.id.frameLayoutContainer,
            fragment = fragment
        )

        if(menuId != null && (binding.bottomNavigationMenu as NavigationBarView).selectedItemId != menuId){
            (binding.bottomNavigationMenu as NavigationBarView).selectedItemId = menuId
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(this) {
            viewModel.curToolbar.value?.animations.let{ curAnimation ->
                viewModel.prevToolbar.value?.animations.let{ prevAnimation ->

                    if (viewModel.curToolbar.value != viewModel.prevToolbar.value){
                        prevAnimation?.animationExiting(this, toolbarBinding)
                        curAnimation?.animationEntering(this, toolbarBinding)
                    }

                    when (it) {
                        is MainViewState.ShowExperienceFeed -> {
                            openMainContainerFragment(ExperienceFeedFragment.newInstance(), it.menuId)
                        }
                        is MainViewState.ShowExperienceStartRecord -> {
                            openMainContainerFragment(ExperienceStartRecordFragment.newInstance(), it.menuId)
                        }
                        is MainViewState.ShowProfile -> {
                            openMainContainerFragment(ProfileFragment.newInstance(), it.menuId)
                        }
                        is MainViewState.SwitchToolbars -> {
                            animateToolbarTransition { viewModel.setCurToolbar(getToolbarById(it.route.id)) }
                        }
                    }
                }
            }
        }
    }

    private fun getToolbarById(id: Int): ToolbarComponentParams? {
        return when(id){
            BottomNavigationBarRoutes.ExperienceFeed.id -> viewModel.experienceFeedToolbar
            BottomNavigationBarRoutes.Profile.id -> viewModel.profileToolbar
            else -> null
        }
    }

    private fun animateToolbarTransition(changeToolbarData: () -> Unit){
        binding.stub.binding?.root?.let{
            it.fadeAnimation(
                false,
                onAnimationEnd = {
                    it.fadeAnimation(true, onAnimationStart = changeToolbarData)
                }
            )
        }
    }

    private fun setupBottomNavigationBar(){
        (binding.bottomNavigationMenu as NavigationBarView).setOnItemSelectedListener { item ->
            viewModel.setOnItemSelectedListenerBottomNavigation(item)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

        with(supportFragmentManager.findFragmentById(R.id.frameLayoutContainer)) {
            if (this is ExperienceFeedFragment && newState == RecyclerView.SCROLL_STATE_IDLE && this.itemClickHappened){
                this.itemClickHappened = false
                this.showLowerExperienceDetailsSheet()
            }
        }

        if (getAppBarHeight() >= recyclerView.computeVerticalScrollOffset()){
            binding.appBarLayout.y = -recyclerView.computeVerticalScrollOffset().toFloat()
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        experienceFeedCurYScroll += dy

        val yOffset: Float =
            if (getAppBarHeight() < experienceFeedCurYScroll.toFloat()) -getAppBarHeight().toFloat()
            else (-experienceFeedCurYScroll.toFloat()).coerceAtMost(0f)

        binding.appBarLayout.y = yOffset

        with(supportFragmentManager.findFragmentById(R.id.frameLayoutContainer)) {
            if (this is ExperienceFeedFragment){
                this.hideLowerExperienceDetailsSheet()
            }
        }
    }

    override fun getAppBarHeight(): Int{
        val tv = TypedValue()
        return if (this.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true))
            TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        else 0
    }

    override fun onBackPressed() {

    }

}