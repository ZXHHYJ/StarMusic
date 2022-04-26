package studio.mandysa.music.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
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

@Composable
fun ControllerScreen() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(navHeight)
    ) {
        val title by PlayManager.changeMusicLiveData().map { return@map it.title }
            .observeAsState("")
        AsyncImage(
            model = PlayManager.changeMusicLiveData().map {
                return@map it.coverUrl
            }.observeAsState(),
            contentDescription = null,
            modifier = Modifier
                .size(28.dp)
                .padding(start = 16.dp)
        )
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .weight(1.0f)
                .fillMaxHeight()
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_play),
            contentDescription = null,
            modifier = Modifier
                .width(55.dp)
                .padding(10.dp)
                .fillMaxHeight()
                .clickable(onClick = {
                    if (PlayManager.pauseLiveData().value == true)
                        PlayManager.play()
                    else PlayManager.pause()
                })
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_skip_next),
            contentDescription = null,
            modifier = Modifier
                .width(55.dp)
                .padding(10.dp)
                .fillMaxHeight()
                .clickable(onClick = {
                    PlayManager.skipToNext()
                })
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

class ControllerFragment : Fragment() {
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