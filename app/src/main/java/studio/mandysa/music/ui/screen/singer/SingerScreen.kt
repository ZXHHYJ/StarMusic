package studio.mandysa.music.ui.screen.singer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.olshevski.navigation.reimagined.*
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.AppDivider
import studio.mandysa.music.ui.common.AppTabRow
import studio.mandysa.music.ui.item.ItemCoverHeader
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.screen.singer.popularsong.PopularSongScreen
import studio.mandysa.music.ui.screen.singer.singeralbum.SingerAlbumScreen
import studio.mandysa.music.ui.theme.onBackground

enum class SingerScreenTabDestination {
    PopularSong, SingerAlbum
}

val SingerScreenTabDestination.tabName: String
    @Composable get() = when (this) {
        SingerScreenTabDestination.PopularSong -> stringResource(id = R.string.popular_song)
        SingerScreenTabDestination.SingerAlbum -> stringResource(id = R.string.singer_album)
    }

@Composable
fun SingerScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    id: String,
    singerViewModel: SingerViewModel = viewModel(factory = viewModelFactory {
        addInitializer(SingerViewModel::class) { SingerViewModel(id) }
    })
) {
    val singerInfo by singerViewModel.singerInfo.observeAsState()
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
        Column {
            ItemCoverHeader(
                dialogNavController = dialogNavController,
                coverUrl = singerInfo?.cover ?: "",
                title = singerInfo?.name ?: "",
                message = singerInfo?.briefDesc ?: ""
            )
            AppDivider()
            var selectedIndex by remember {
                mutableStateOf(0)
            }
            val navController =
                rememberNavController(startDestination = SingerScreenTabDestination.PopularSong)
            AppTabRow(
                selectedTabIndex = selectedIndex,
            ) {
                SingerScreenTabDestination.values().forEachIndexed { index, destination ->
                    Tab(
                        selected = selectedIndex == index,
                        text = {
                            Text(text = destination.tabName)
                        },
                        onClick = {
                            selectedIndex = index
                            if (!navController.moveToTop { it == destination }) {
                                navController.navigate(destination)
                            }
                        })
                }
            }
            NavHost(navController) {
                when (it) {
                    SingerScreenTabDestination.PopularSong -> {
                        PopularSongScreen(
                            dialogNavController = dialogNavController,
                            paddingValues = paddingValues,
                            id = id
                        )
                    }
                    SingerScreenTabDestination.SingerAlbum -> {
                        SingerAlbumScreen(
                            mainNavController = mainNavController,
                            paddingValues = paddingValues,
                            id = id
                        )
                    }
                }
            }
        }
    }
}