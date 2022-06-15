package studio.mandysa.music.ui.screen.me

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import studio.mandysa.music.R
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.ItemTitle
import studio.mandysa.music.ui.item.PlaylistItem
import studio.mandysa.music.ui.screen.me.like.LikeScreen
import studio.mandysa.music.ui.screen.playlist.PlaylistScreen
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.round
import studio.mandysa.music.ui.theme.verticalMargin

@Composable
fun MenuItem(title: String, imageVector: ImageVector, onClick: () -> Unit) {
    FilledTonalButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = horizontalMargin),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.weight(1.0f),
            text = title, fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(imageVector = imageVector, contentDescription = null)
    }
    Spacer(modifier = Modifier.height(verticalMargin))
}

@Composable
fun MeScreen(meViewModel: MeViewModel = viewModel(), paddingValues: PaddingValues) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            val userInfo by meViewModel.userInfo.observeAsState()
            val playlist by meViewModel.allPlaylist.observeAsState(listOf())
            LazyColumn {
                item {
                    ItemTitle(stringResource(R.string.me))
                }
                item {
                    userInfo?.InfoCard()
                }
                item {
                    ItemSubTitle(stringResource(R.string.me_playlist))
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = horizontalMargin),
                        horizontalArrangement = Arrangement.spacedBy(horizontalMargin / 2)
                    ) {
                        items(playlist) {
                            PlaylistItem(title = it.name, coverUrl = it.coverImgUrl) {
                                navController.navigate((if (playlist[0] == it) "like" else "playlist") + "/${it.id}")
                            }
                        }
                    }
                }
                item {
                    ItemSubTitle(stringResource(R.string.recently_played))
                }
                item {
                    ItemSubTitle(stringResource(R.string.more))
                }
                item {
                    MenuItem(
                        title = stringResource(id = R.string.setting),
                        imageVector = Icons.Rounded.Favorite
                    ) {

                    }
                }
                item {
                    MenuItem(
                        title = stringResource(id = R.string.about),
                        imageVector = Icons.Rounded.Info
                    ) {

                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(paddingValues))
                }
            }
        }
        composable("like/{id}") {
            LikeScreen(navController = navController, id = it.arguments?.getString("id")!!)
        }
        composable("playlist/{id}") {
            PlaylistScreen(navController = navController, id = it.arguments?.getString("id")!!)
        }
        composable("setting") {
            SettingScreen()
        }
        composable("about") {
            AboutScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserModel.InfoCard() {
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
                        model = avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
            Column {
                Text(text = nickname)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = signature)
            }
        }
    }
}