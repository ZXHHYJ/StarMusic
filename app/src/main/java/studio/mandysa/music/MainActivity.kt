package studio.mandysa.music

import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.drake.spannable.addSpan
import com.drake.spannable.setSpan
import com.sothree.slidinguppanel.PanelSlideListener
import com.sothree.slidinguppanel.PanelState
import com.yanzhenjie.sofia.Sofia
import studio.mandysa.music.databinding.ActivityMainBinding
import studio.mandysa.music.databinding.LayoutStartBinding
import studio.mandysa.music.logic.ktx.createFragmentStateAdapter
import studio.mandysa.music.logic.ktx.viewBinding
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.login.LoginFragment
import studio.mandysa.music.ui.me.MeFragment


class MainActivity : AppCompatActivity() {

    @Composable
    fun MainCompose() {
        Column() {

        }
    }

    private val mBinding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        Sofia.with(this).let {
            it.invasionStatusBar().invasionNavigationBar()
            it.statusBarDarkFont().navigationBarDarkFont()
        }
        mBinding.mainStateLayout.showEmpty {
            val layoutStartBinding = LayoutStartBinding.bind(this)
            layoutStartBinding.slogan.text = getString(R.string.login_to).setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        context,
                        R.color.translucent_white
                    )
                )
            ).addSpan(context.getString(R.string.app_name))
            layoutStartBinding.btnLogin.setOnClickListener {
                LoginFragment().show(supportFragmentManager, null)
            }
            layoutStartBinding.btnExit.setOnClickListener {
                finish()
            }
        }
        mBinding.mainStateLayout.showEmptyState()
        mBinding.apply {
            mainFragmentPage.isUserInputEnabled = false
            mainFragmentPage.adapter = createFragmentStateAdapter(
                NavHostFragment.create(R.navigation.fragment_home_navigaction),
                NavHostFragment.create(R.navigation.fragment_search_navigation),
                MeFragment()
            )
            (mainBottomNav ?: mainNavRail)?.setOnItemSelectedListener { item ->
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
                mainFragmentPage.setPadding(0, startBarSize, 0, 0)
                mainBottomNav?.setPadding(
                    0,
                    0,
                    0,
                    navigationBarSize
                )
                mainBottomNav?.layoutParams?.height =
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
                                if (mainBottomNav != null) {
                                    val by: Float =
                                        y + mainBottomNav.height * slideOffset * 8
                                    if (by < y) return
                                    mainBottomNav.y = by
                                }
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
                                        if (mBinding.mainPlayBackground.background == null)
                                            mBinding.mainPlayBackground.background =
                                                ContextCompat.getDrawable(
                                                    this@MainActivity,
                                                    android.R.color.white
                                                )
                                    }
                                    else -> {
                                        mBinding.mainPlayBackground.background = null
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