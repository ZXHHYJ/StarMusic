package studio.mandysa.music.ui.screen.local.album

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import kotlinx.coroutines.launch
import studio.mandysa.music.R
import studio.mandysa.music.logic.repository.LocalMediaRepository
import studio.mandysa.music.ui.common.MediaPermission
import studio.mandysa.music.ui.common.SearchBar
import studio.mandysa.music.ui.item.AlbumItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.isMatePad
import studio.mandysa.music.ui.theme.onBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    drawerState: DrawerState,
    padding: PaddingValues
) {
    val coroutineScope = rememberCoroutineScope()
    MediaPermission(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val albums = LocalMediaRepository.getAlbums()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = padding
        ) {
            item {
                SearchBar(click = { mainNavController.navigate(ScreenDestination.Search) }) {
                    Icon(
                        imageVector = if (isMatePad) Icons.Rounded.Search else Icons.Rounded.Menu,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                }
                            }
                        },
                        tint = onBackground
                    )
                    Text(text = stringResource(id = R.string.search_hint))
                }
            }
            itemsIndexed(albums) { _, item ->
                AlbumItem(album = item) {

                }
            }
        }
    }
}