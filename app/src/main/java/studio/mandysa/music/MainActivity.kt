package studio.mandysa.music

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yanzhenjie.sofia.Sofia
import com.zj.statelayout.ComposeStateLayout
import com.zj.statelayout.PageState
import com.zj.statelayout.PageStateData
import studio.mandysa.music.ui.common.MultiBottomSheetLayout
import studio.mandysa.music.ui.screen.LoginScreen
import studio.mandysa.music.ui.screen.MainScreen
import studio.mandysa.music.ui.screen.StartScreen
import studio.mandysa.music.ui.theme.MandySaMusicTheme
import studio.mandysa.music.ui.viewmodel.EventViewModel

class MainActivity : AppCompatActivity() {

    private val mEvent by viewModels<EventViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MandySaMusicTheme {
                var pageStateData by remember {
                    mutableStateOf(PageStateData(PageState.CONTENT))
                }
                mEvent.getCookieLiveData().observe(this) {
                    if (it == null) {
                        pageStateData = PageStateData(PageState.EMPTY)
                        return@observe
                    }
                    pageStateData = PageStateData(PageState.CONTENT)
                }
                ComposeStateLayout(
                    modifier = Modifier.fillMaxSize(),
                    pageStateData = pageStateData,
                    empty = {
                        MultiBottomSheetLayout(
                            sheetElevation = 10.dp,
                            sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                            sheetContent = {
                                LoginScreen()
                            }, mainContent = { openSheet ->
                                StartScreen(openSheet)
                            })
                    },
                    content = {
                        MainScreen()
                    }
                )
            }
        }
        Sofia.with(this).apply {
            invasionStatusBar().invasionNavigationBar()
            statusBarDarkFont().navigationBarDarkFont()
        }
    }

}