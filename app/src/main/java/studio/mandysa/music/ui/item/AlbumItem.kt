package studio.mandysa.music.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.LocalMediaRepository.songs
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.service.playmanager.ktx.coverUrl
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.AppCard
import studio.mandysa.music.ui.common.BoxWithPercentages
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.textColorLight

/**
 * @author 黄浩
 */
@Composable
fun AlbumItem(
    modifier: Modifier = Modifier,
    album: SongBean.Local.Album,
    onClick: () -> Unit
) {
    AppCard(backgroundColor = Color.Transparent) {
        BoxWithPercentages(modifier = modifier) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppCard(
                    backgroundColor = Color.Transparent,
                    modifier = Modifier.size(maxWidth)
                ) {
                    AppAsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        url = album.coverUrl,
                        onClick = onClick
                    )
                }
                Text(
                    text = album.name,
                    color = textColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = stringResource(id = R.string.total_n_songs, album.songs.size),
                    color = textColorLight,
                    fontSize = 11.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}