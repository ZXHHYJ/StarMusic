package studio.mandysa.music.ui.screen.cloud.singercnt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.ui.item.ContentColumnItem
import studio.mandysa.music.ui.item.SubTitleItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.onBackground

@Composable
fun CloudSingerCntScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    cloudSingerCntViewModel: CloudSingerCntViewModel = viewModel(factory = viewModelFactory {
        addInitializer(CloudSingerCntViewModel::class) { CloudSingerCntViewModel(id) }
    })
) {
    val singerInfo by cloudSingerCntViewModel.singerInfo.observeAsState()
    val songs by cloudSingerCntViewModel.songs.observeAsState()
    Column {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            contentColor = onBackground,
            elevation = 0.dp
        ) {
            IconButton(onClick = { mainNavController.pop() }) {
                Icon(Icons.Rounded.ArrowBack, null)
            }
        }
        LazyColumn {
            item {
                ContentColumnItem(
                    dialogNavController = dialogNavController,
                    coverUrl = singerInfo?.cover ?: "",
                    title = singerInfo?.name ?: "",
                    message = singerInfo?.briefDesc ?: ""
                ) {
                    //no toolbar
                }
            }
            item {
                SubTitleItem(stringResource(id = R.string.album))
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = horizontalMargin),
                    horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2)
                ) {
                    /*  items(albums) {
                          AlbumItem(mateAlbum = it!!) {
                              mainNavController.navigate(ScreenDestination.Album(it.id))
                          }
                      }*/
                }
            }
            item {
                SubTitleItem(stringResource(id = R.string.popular_song))
            }
            songs?.let { list ->
                items(list.size) {
                    /*SongItem(dialogNavController = dialogNavController, song = list[it]) {
                        PlayManager.play(list, it)
                    }*/
                }
            }
            item {
                Spacer(modifier = Modifier.padding(paddingValues))
            }
        }
    }
}