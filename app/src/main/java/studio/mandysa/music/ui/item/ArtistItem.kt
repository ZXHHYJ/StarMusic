package studio.mandysa.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.LocalMediaRepository.songs
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.service.playmanager.ktx.coverUrl
import studio.mandysa.music.ui.common.AppRoundAsyncImage
import studio.mandysa.music.ui.theme.smallHorizontal
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.textColorLight
import studio.mandysa.music.ui.theme.smallVertical

@Composable
fun ArtistItem(artist: SongBean.Local.Artist, onClick: () -> Unit) {
    ArtistItem(
        coverUrl = artist.coverUrl,
        title = artist.name,
        subTitle = stringResource(id = R.string.total_n_songs, artist.songs.size)
    ) {
        onClick.invoke()
    }
}

@Composable
fun ArtistItem(coverUrl: String, title: String, subTitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = smallHorizontal, vertical = smallVertical)
                .size(50.dp), contentAlignment = Alignment.Center
        ) {
            AppRoundAsyncImage(modifier = Modifier.size(50.dp), url = coverUrl)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(vertical = smallVertical),
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
        /*Spacer(modifier = Modifier.padding(end = horizontalMargin))*/
    }
}

@Preview
@Composable
private fun PreviewArtistItem() {
    ArtistItem(
        coverUrl = "https://ts1.cn.mm.bing.net/th?id=OIP-C.yt_a69g76iIJXsi22XGlNgHaE5&w=166&h=185&c=8&rs=1&qlt=90&o=6&dpr=1.8&pid=3.1&rm=2",
        title = "Chen Yuan Xi",
        subTitle = "陈元汐"
    ) {

    }
}