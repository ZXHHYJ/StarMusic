package studio.mandysa.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.LocalMediaRepository.artist
import studio.mandysa.music.logic.repository.LocalMediaRepository.songs
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.service.playmanager.ktx.coverUrl
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.AppCard
import studio.mandysa.music.ui.theme.*

@Composable
fun AlbumItem(
    album: SongBean.Local.Album,
    onClick: () -> Unit
) {
    AppCard(backgroundColor = Color.Transparent) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = horizontal, vertical = vertical)
                    .size(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = 5.dp),
                    backgroundColor = Color.LightGray,
                    shape = RoundedCornerShape(50),
                    elevation = 0.dp
                ) {
                    //半个专辑的背景
                }
                AppCard(backgroundColor = Color.Transparent) {
                    AppAsyncImage(modifier = Modifier.fillMaxSize(), url = album.coverUrl)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.0f)
                    .padding(vertical = vertical),
            ) {
                Text(
                    text = album.name,
                    color = textColor,
                    fontSize = 15.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = "${album.artist.name} ${
                        stringResource(
                            id = R.string.total_n_songs,
                            album.songs.size
                        )
                    }",
                    color = textColorLight,
                    fontSize = 13.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}