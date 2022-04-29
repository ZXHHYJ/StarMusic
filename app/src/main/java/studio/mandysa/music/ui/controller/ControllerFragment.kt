package studio.mandysa.music.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.map
import coil.compose.AsyncImage
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.ui.theme.MandySaMusicTheme
import studio.mandysa.music.ui.theme.navHeight

class ControllerFragment : Fragment() {

    @Composable
    fun ControllerScreen() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(navHeight)
                .padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = 0.dp,
                modifier = Modifier
                    .size(48.dp)
            ) {
                val coverUrl by PlayManager.changeMusicLiveData().map { return@map it.coverUrl }
                    .observeAsState()
                AsyncImage(
                    model = coverUrl,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentDescription = null
                )
            }
            val title by PlayManager.changeMusicLiveData().map { return@map it.title }
                .observeAsState("")
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1.0f)
            )
            var playAndPauseImg by remember {
                mutableStateOf(R.drawable.ic_play)
            }
            PlayManager.pauseLiveData().map {
                playAndPauseImg = if (it) R.drawable.ic_play else R.drawable.ic_pause
            }.observeAsState()
            Image(
                painter = painterResource(playAndPauseImg),
                contentDescription = null,
                modifier = Modifier
                    .width(55.dp)
                    .padding(10.dp)
                    .fillMaxHeight()
                    .clickable {
                        if (PlayManager.pauseLiveData().value == true)
                            PlayManager.play()
                        else PlayManager.pause()
                    }
            )
            Image(
                painter = painterResource(R.drawable.ic_skip_next),
                contentDescription = null,
                modifier = Modifier
                    .width(55.dp)
                    .padding(10.dp)
                    .fillMaxHeight()
                    .clickable {
                        PlayManager.skipToNext()
                    }
            )
        }
    }

    @Preview
    @Composable
    fun PreviewPlayScreen() {
        MandySaMusicTheme {
            ControllerScreen()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            it.setContent {
                ControllerScreen()
            }
        }
    }
}