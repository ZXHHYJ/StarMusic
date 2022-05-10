package studio.mandysa.music

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yanzhenjie.sofia.Sofia
import com.zj.statelayout.ComposeStateLayout
import com.zj.statelayout.PageState
import com.zj.statelayout.PageStateData
import studio.mandysa.music.ui.screen.MainScreen
import studio.mandysa.music.ui.viewmodel.EventViewModel

class MainActivity : AppCompatActivity() {

    private val mEvent by viewModels<EventViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                onRetry = {

                },
                loading = {

                },
                empty = {
                    Text("test")
                },
                error = {

                },
                content = {
                    MainScreen()
                }
            )
        }
        Sofia.with(this).apply {
            invasionStatusBar().invasionNavigationBar()
            statusBarDarkFont().navigationBarDarkFont()
        }
    }

}