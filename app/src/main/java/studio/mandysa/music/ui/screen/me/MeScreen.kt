package studio.mandysa.music.ui.screen.me

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import studio.mandysa.music.R
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.ItemTitle
import studio.mandysa.music.ui.screen.me.meplaylist.MePlaylistScreen
import studio.mandysa.music.ui.screen.playlist.PlaylistScreen
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.round
import studio.mandysa.music.ui.theme.verticalMargin

const val main = "main"
const val iLike = "i_like"
const val playlist = "playlist"
const val setting = "setting"
const val about = "about"
const val mePlaylist = "me_playlist"
const val recentlyPlayed = "recently_played"

data class MenuItem(@StringRes val id: Int, val imageVector: ImageVector, val route: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Main(navController: NavHostController, me: MeViewModel = viewModel()) {
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
                        navController.navigate(model.route)
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
                MenuItem(R.string.i_like, Icons.Rounded.Favorite, iLike),
                MenuItem(R.string.recently_played, Icons.Rounded.AccessTime, recentlyPlayed),
                MenuItem(R.string.me_playlist, Icons.Rounded.PlaylistPlay, mePlaylist)
            )
        )
        item(span = { GridItemSpan(cols) }) {
            ItemSubTitle(stringResource(R.string.setting))
        }
        menus(
            listOf(
                MenuItem(R.string.setting, Icons.Rounded.Favorite, setting),
                MenuItem(R.string.about, Icons.Rounded.Info, about),
            )
        )
    }
}

@Composable
fun MeScreen() {
    val navController = rememberNavController()
    NavHost(
        navController,
        startDestination = main,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        composable(main) { Main(navController) }
        composable(iLike) {
            ILikeScreen()
        }
        composable(recentlyPlayed) {
            RecentlyPlayedScreen()
        }
        composable(mePlaylist) {
            MePlaylistScreen(navController)
        }
        composable("$playlist/{id}") { backStackEntry ->
            PlaylistScreen(
                navController = navController,
                id = backStackEntry.arguments!!.getString("id", "")
            )
        }
        composable(setting) {
            SettingScreen()
        }
        composable(about) {
            AboutScreen()
        }
    }
}