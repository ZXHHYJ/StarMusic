package studio.mandysa.music.ui.screen.songmenu


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll
import kotlinx.parcelize.RawValue
import studio.mandysa.music.R
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.service.playmanager.model.MetaMusic
import studio.mandysa.music.ui.common.CardAsyncImage
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongMenu(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    model: @RawValue MetaMusic<*, *>,
    songMenuViewModel: SongMenuViewModel = viewModel(factory = viewModelFactory {
        addInitializer(SongMenuViewModel::class) { SongMenuViewModel(model.id) }
    })
) {
    val isLike by songMenuViewModel.liked.observeAsState()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = cornerShape,
        colors = CardDefaults.cardColors(containerColor = dialogBackground)
    ) {
        LazyColumn {
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
                        CardAsyncImage(size = 80.dp, url = model.coverUrl)
                        Column(modifier = Modifier.padding(verticalMargin)) {
                            Text(
                                text = model.title,
                                color = textColor,
                                fontSize = 15.sp,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.weight(1.0f))
                            Text(
                                text = model.artist.allArtist(),
                                color = textColorLight,
                                fontSize = 13.sp,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
            item {
                MenuItem(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    title = stringResource(id = if (isLike == true) R.string.remove_like else R.string.add_like),
                    imageVector = if (isLike == true) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    enabled = isLike != null
                ) {
                    songMenuViewModel.likeMusic(isLike == false)
                }
            }
            item {
                MenuItem(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    title = "${stringResource(id = R.string.album)}:${model.album.name}",
                    imageVector = Icons.Rounded.Album
                ) {
                    dialogNavController.popAll()
                    mainNavController.navigate(ScreenDestination.Album(model.album.id))
                }
            }
            item {
                MenuItem(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    title = stringResource(id = R.string.next_play),
                    imageVector = Icons.Rounded.Add
                ) {

                }
            }
            items(model.artist) {
                MenuItem(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    title = "${stringResource(id = R.string.singer)}:${it.name}",
                    imageVector = Icons.Rounded.Person
                ) {
                    dialogNavController.popAll()
                    mainNavController.navigate(ScreenDestination.Singer(it.id))
                }
            }
        }
    }
}