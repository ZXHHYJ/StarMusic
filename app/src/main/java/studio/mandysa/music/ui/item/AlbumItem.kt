package studio.mandysa.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.LocalMediaRepository.artist
import studio.mandysa.music.logic.repository.LocalMediaRepository.songs
import studio.mandysa.music.service.playmanager.bean.MetaAlbum
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.service.playmanager.ktx.coverUrl
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

private fun stampToDate(s: String): String {
    val date = Date(s.toLong())
    return SimpleDateFormat.getDateInstance().format(date)
}

@Composable
fun AlbumItem(mateAlbum: MetaAlbum, onClick: () -> Unit) {
    AlbumItem(
        coverUrl = mateAlbum.coverUrl,
        title = mateAlbum.name,
        subTitle = stampToDate(mateAlbum.publishTime)
    ) {
        onClick.invoke()
    }
}

@Composable
fun AlbumItem(album: SongBean.Local.Album, onClick: () -> Unit) {
    AlbumItem(
        coverUrl = album.coverUrl,
        title = album.name,
        subTitle = "${album.artist.name} ${
            stringResource(
                id = R.string.total_n_songs,
                album.songs.size
            )
        }",
        onClick = onClick
    )
}

@Composable
private fun AlbumItem(coverUrl: String, title: String, subTitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val coverSize = 50.dp
        Box(
            modifier = Modifier
                .padding(horizontal = horizontalMargin, vertical = verticalMargin)
                .size(coverSize),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = 5.dp),
                colors = CardDefaults.cardColors(Color.LightGray),
                shape = RoundedCornerShape(coverSize)
            ) {
                //半个专辑的背景
            }
            AppAsyncImage(modifier = Modifier.size(coverSize), url = coverUrl)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(vertical = verticalMargin),
        ) {
            Text(
                text = title,
                color = textColor,
                fontSize = 15.sp,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = subTitle,
                color = textColorLight,
                fontSize = 13.sp,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            tint = onBackground,
            contentDescription = null,
            modifier = Modifier
                .clickable {

                }
        )
        Spacer(modifier = Modifier.padding(end = horizontalMargin))
    }
}

@Preview
@Composable
private fun PreviewAlbumItem() {
    AlbumItem(
        coverUrl = "https://imgo.114shouji.com/img2021/1/27/9/2021012768987162.jpg",
        title = "东方镜",
        subTitle = "2022/11/19"
    ) {

    }
}