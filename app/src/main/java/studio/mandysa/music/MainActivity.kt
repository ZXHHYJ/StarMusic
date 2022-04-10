package studio.mandysa.music

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.sothree.slidinguppanel.PanelSlideListener
import com.sothree.slidinguppanel.PanelState
import com.yanzhenjie.sofia.Sofia
import studio.mandysa.music.databinding.ActivityMainBinding
import studio.mandysa.music.logic.ktx.createFragmentStateAdapter
import studio.mandysa.music.logic.ktx.viewBinding
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.me.MeFragment


class MainActivity : AppCompatActivity() {

    private val mBinding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        Sofia.with(this).invasionStatusBar().invasionNavigationBar()
        mBinding.apply {
            mainFragmentPage.isUserInputEnabled = false
            mainFragmentPage.adapter = createFragmentStateAdapter(
                NavHostFragment.create(R.navigation.fragment_home_navigaction),
                NavHostFragment.create(R.navigation.fragment_search_navigation),
                MeFragment()
            )
            mainBottomView.setOnItemSelectedListener { item ->
                mainFragmentPage.setCurrentItem(
                    when (item.itemId) {
                        R.id.navigation_home -> 0
                        R.id.navigation_search -> 1
                        R.id.navigation_me -> 2
                        else -> 0
                    }, false
                )
                true
            }
            ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
                val startBarSize = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                val navigationBarSize =
                    insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                statusBarBlurBackground.layoutParams.height = startBarSize
                mainBottomView.setPadding(
                    0,
                    0,
                    0,
                    navigationBarSize
                )
                mainBottomNavLayout.layoutParams.height =
                    resources.getDimensionPixelSize(
                        R.dimen.nav_height
                    ) + navigationBarSize
                PlayManager.changeMusicLiveData()
                    .observe(this@MainActivity) { _ ->
                        mainSlidingView.panelHeight =
                            resources.getDimensionPixelSize(R.dimen.controller_height) + resources.getDimensionPixelSize(
                                R.dimen.nav_height
                            ) + navigationBarSize
                        val y =
                            mBinding.root.bottom - (resources.getDimensionPixelSize(R.dimen.nav_height) + navigationBarSize)
                        object : PanelSlideListener {
                            override fun onPanelSlide(panel: View, slideOffset: Float) {
                                val by: Float =
                                    y + mainBottomNavLayout.height * slideOffset * 8
                                if (by < y) return
                                mainBottomNavLayout.y = by
                                val alpha = slideOffset * 40
                                playFragment.alpha = alpha
                                controllerFragment.alpha = (1 - alpha).also {
                                    mBinding.controllerFragment.visibility =
                                        if (it > 0f) View.VISIBLE else View.GONE
                                }
                            }

                            override fun onPanelStateChanged(
                                panel: View,
                                previousState: PanelState,
                                newState: PanelState
                            ) {
                                when (newState) {
                                    PanelState.EXPANDED -> {
                                        Sofia.with(this@MainActivity)
                                            .statusBarLightFont()
                                    }
                                    PanelState.DRAGGING -> {
                                        if (mBinding.playBackground.background == null)
                                            mBinding.playBackground.background =
                                                ContextCompat.getDrawable(
                                                    this@MainActivity,
                                                    android.R.color.white
                                                )
                                    }
                                    else -> {
                                        mBinding.playBackground.background = null
                                        Sofia.with(this@MainActivity)
                                            .statusBarDarkFont()
                                    }
                                }
                            }
                        }.also { listener ->
                            if (mainSlidingView.panelSlideListenerList.size == 0) {
                                mainSlidingView.panelSlideListenerList.add(listener)
                            } else mainSlidingView.panelSlideListenerList[0] = listener
                        }
                    }
                insets
            }
        }
    }

    override fun onBackPressed() {
        if (mBinding.mainSlidingView.panelState == PanelState.EXPANDED) {
            mBinding.mainSlidingView.panelState = PanelState.COLLAPSED
        } else super.onBackPressed()
    }

}