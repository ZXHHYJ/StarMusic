package studio.mandysa.music.ui.screen.local.singercnt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.LocalMediaRepository.songs
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.ui.common.AppRoundCard
import studio.mandysa.music.ui.common.BoxWithPercentages
import studio.mandysa.music.ui.item.SongItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.background
import studio.mandysa.music.ui.theme.textColor

@Composable
fun SingerCntScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    artist: SongBean.Local.Artist
) {
    BoxWithPercentages(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                PlayAllButton(modifier = Modifier)
                SingerNameTitle(modifier = Modifier, singerName = artist.name)
                MoreButton(modifier = Modifier)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
            ) {
                itemsIndexed(artist.songs) { index, item ->
                    SongItem(dialogNavController = dialogNavController, song = item) {
                        PlayManager.play(artist.songs, index)
                    }
                }
            }
        }
    }
}

@Composable
private fun SingerNameTitle(modifier: Modifier, singerName: String) {
    Text(text = singerName, color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun PlayAllButton(modifier: Modifier) {
    AppRoundCard(modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_play),
            contentDescription = null,
            tint = background
        )
    }
}

@Composable
private fun MoreButton(modifier: Modifier) {
    AppRoundCard(modifier = modifier) {
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            tint = background
        )
    }
}