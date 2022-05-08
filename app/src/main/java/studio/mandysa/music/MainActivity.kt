package studio.mandysa.music

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.yanzhenjie.sofia.Sofia
import studio.mandysa.music.ui.screen.MainScreen


class MainActivity : AppCompatActivity() {

/*    private val mBinding: ActivityMainBinding by viewBinding()

    private val mEvent by viewModels<EventViewModel>()

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
        if (mEvent.getCookieLiveData().value == null) {
            mBinding.mainStateLayout.showEmptyState()
            mEvent.getCookieLiveData().observe(this) {
                if (it != null)
                    mBinding.mainStateLayout.showContentState()
            }
        } else mBinding.mainStateLayout.showContentState()
        mBinding.run {
            mainComposeView.setContent {
                MainScreen()
            }
            ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
                val startBarSize = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                val navigationBarSize =
                    insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                mainComposeView.setPadding(
                    0,
                    startBarSize,
                    0,
                    if (mainBottomNav != null) resources.getDimensionPixelSize(
                        R.dimen.nav_height
                    ) + navigationBarSize else 0
                )
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
                        mainComposeView.setPadding(
                            0,
                            startBarSize,
                            0,
                            0
                        )
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
                                controllerFragment.alpha = 1 - alpha
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
                                        controllerFragment.visibility = View.GONE
                                    }
                                    PanelState.DRAGGING -> {
                                        if (mainPlayBackground.background == null)
                                            mainPlayBackground.background =
                                                ContextCompat.getDrawable(
                                                    this@MainActivity,
                                                    android.R.color.white
                                                )
                                        controllerFragment.visibility = View.VISIBLE
                                    }
                                    else -> {
                                        Sofia.with(this@MainActivity)
                                            .statusBarDarkFont()
                                        mainPlayBackground.background = null
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
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
        Sofia.with(this).let {
            it.invasionStatusBar().invasionNavigationBar()
            it.statusBarDarkFont().navigationBarDarkFont()
        }
    }

}