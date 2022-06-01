package studio.mandysa.music.ui.screen.me

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.rememberNavController
import kotlinx.parcelize.Parcelize
import studio.mandysa.music.R
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.ItemTitle
import studio.mandysa.music.ui.screen.me.ilike.ILikeScreen
import studio.mandysa.music.ui.screen.me.meplaylist.MePlaylistScreen
import studio.mandysa.music.ui.screen.playlist.PlaylistScreen
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.round
import studio.mandysa.music.ui.theme.verticalMargin

sealed class MeScreenDestination : Parcelable {
    @Parcelize
    object Main : MeScreenDestination()

    @Parcelize
    object ILike : MeScreenDestination()

    @Parcelize
    object Setting : MeScreenDestination()

    @Parcelize
    object MePlaylist : MeScreenDestination()

    @Parcelize
    object RecentlyPlayed : MeScreenDestination()

    @Parcelize
    object About : MeScreenDestination()

    @Parcelize
    data class Playlist(val id: String) : MeScreenDestination()
}

private data class MenuItem(
    @StringRes val id: Int,
    val imageVector: ImageVector,
    val route: MeScreenDestination
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Main(me: MeViewModel = viewModel()) {
    fun LazyGridScope.menus(list: List<MenuItem>) {
        itemsIndexed(list) { pos, model ->
            Box(modifier = Modifier.padding(top = if (pos > 1) verticalMargin else 0.dp).run {
                if (pos % 2 == 0) {
                    padding(
                        start = horizontalMargin,
                        end = horizontalMargin / 2
                    )
                } else {
                    padding(
                        start = horizontalMargin / 2,
                        end = horizontalMargin
                    )
                }
            }) {
                FilledTonalButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp), onClick = {
                        /*navController.navigate(model.route)*/
                    }
                ) {
                    Text(
                        modifier = Modifier.weight(1.0f),
                        text = stringResource(model.id), fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(imageVector = model.imageVector, contentDescription = null)
                }
            }
        }
    }
    me.refresh()
    val userInfo by me.getUserInfo().observeAsState()
    val cols = 2
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(cols)
    ) {
        item(span = { GridItemSpan(cols) }) {
            ItemTitle(stringResource(R.string.me))
        }
        userInfo?.let {
            item(span = { GridItemSpan(cols) }) {
                Card(
                    modifier = Modifier.padding(horizontal = horizontalMargin),
                    shape = RoundedCornerShape(round)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                            Card(
                                modifier = Modifier
                                    .size(70.dp),
                                shape = RoundedCornerShape(35.dp)
                            ) {
                                AsyncImage(
                                    model = it.avatarUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                        }
                        Column {
                            Text(text = it.nickname)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = it.signature)
                        }
                    }
                }
            }
        }
        item(span = { GridItemSpan(cols) }) {
            ItemSubTitle("More")
        }
        menus(
            listOf(
                MenuItem(R.string.i_like, Icons.Rounded.Favorite, MeScreenDestination.ILike),
                MenuItem(
                    R.string.recently_played,
                    Icons.Rounded.AccessTime,
                    MeScreenDestination.RecentlyPlayed
                ),
                MenuItem(
                    R.string.me_playlist,
                    Icons.Rounded.PlaylistPlay,
                    MeScreenDestination.MePlaylist
                )
            )
        )
        item(span = { GridItemSpan(cols) }) {
            ItemSubTitle(stringResource(R.string.setting))
        }
        menus(
            listOf(
                MenuItem(R.string.setting, Icons.Rounded.Favorite, MeScreenDestination.Setting),
                MenuItem(R.string.about, Icons.Rounded.Info, MeScreenDestination.About),
            )
        )
    }
}

@Composable
fun MeScreen() {
    val navController =
        rememberNavController<MeScreenDestination>(startDestination = MeScreenDestination.Main)
    NavHost(navController) { screen ->
        when (screen) {
            MeScreenDestination.Main -> Main()
            MeScreenDestination.ILike -> ILikeScreen(navController)
            MeScreenDestination.RecentlyPlayed -> RecentlyPlayedScreen()
            MeScreenDestination.MePlaylist -> MePlaylistScreen(navController)
            MeScreenDestination.Setting -> SettingScreen()
            MeScreenDestination.About -> AboutScreen()
            is MeScreenDestination.Playlist -> PlaylistScreen(
                navController = navController,
                id = screen.id
            )
        }
    }
}