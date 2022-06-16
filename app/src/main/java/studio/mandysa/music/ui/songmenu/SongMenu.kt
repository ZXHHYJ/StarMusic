package studio.mandysa.music.ui.songmenu


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.ui.common.CardAsyncImage
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.textColorLight
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
fun SongMenu(
    id: String,
    songMenuViewModel: SongMenuViewModel = viewModel(factory = viewModelFactory {
        addInitializer(SongMenuViewModel::class) { SongMenuViewModel(id) }
    })
) {
    val songInfo by songMenuViewModel.songInfo.observeAsState()
    LazyColumn(modifier = Modifier.navigationBarsPadding()) {
        songInfo?.let {
            item {
                Box(
                    modifier = Modifier.padding(
                        horizontal = horizontalMargin,
                        vertical = verticalMargin
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .height(80.dp)
                    ) {
                        CardAsyncImage(size = 80.dp, url = it.coverUrl)
                        Column(modifier = Modifier.padding(verticalMargin)) {
                            Text(text = it.title, color = textColor, fontSize = 15.sp, maxLines = 1)
                            Spacer(modifier = Modifier.weight(1.0f))
                            Text(
                                text = it.artist.allArtist(),
                                color = textColorLight,
                                fontSize = 13.sp,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
        }
        item {
            MenuItem(
                title = stringResource(id = R.string.add_like),
                imageVector = Icons.Rounded.FavoriteBorder
            ) {

            }
        }
        item {
            MenuItem(
                title = stringResource(id = R.string.album),
                imageVector = Icons.Rounded.Album
            ) {

            }
        }
        item {
            MenuItem(
                title = stringResource(id = R.string.next_play),
                imageVector = Icons.Rounded.Add
            ) {

            }
        }
        item {
            MenuItem(
                title = stringResource(id = R.string.singer),
                imageVector = Icons.Rounded.Person
            ) {

            }
        }
    }
}