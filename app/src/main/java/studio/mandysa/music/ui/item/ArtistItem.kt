package studio.mandysa.music.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import studio.mandysa.music.service.playmanager.bean.SongBean

@Composable
fun ArtistItem(index: Int, artist: SongBean.Local.Artist, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = index.toString())
        Column {
            Text(text = artist.name)
            Text(text = "共${artist.songs.size}首")
        }
    }
}

@Preview
@Composable
private fun PreviewArtistItem() {
    ArtistItem(index = 1, artist = SongBean.Local.Artist(0, "啦啦啦", arrayListOf())) {

    }
}