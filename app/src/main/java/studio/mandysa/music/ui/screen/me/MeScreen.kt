package studio.mandysa.music.ui.screen.me

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

/*sealed class MeScreenDestination : Parcelable {
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
}*/

/*private data class MenuItem(
    @StringRes val id: Int,
    val imageVector: ImageVector,
    val route: MeScreenDestination
)*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Main(navController: NavController, me: MeViewModel = viewModel()) {
  /*  fun LazyGridScope.menus(list: List<MenuItem>) {
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
    }*/
}

@Composable
fun MeScreen() {/*
    val navController =
        rememberNavController<MeScreenDestination>(startDestination = MeScreenDestination.Main)
    NavBackHandler(navController)
    NavHost(navController) { screen ->
        when (screen) {
            MeScreenDestination.Main -> Main(navController)
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
    }*/
}