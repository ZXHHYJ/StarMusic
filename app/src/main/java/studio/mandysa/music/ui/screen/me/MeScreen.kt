package studio.mandysa.music.ui.screen.me

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.R
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.item.ItemSubTitle
import studio.mandysa.music.ui.item.ItemTitle
import studio.mandysa.music.ui.item.PlaylistItem
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.round

@Composable
fun MeScreen(
    mainNavController: NavController<ScreenDestination>,
    meViewModel: MeViewModel = viewModel()
) {
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
                        if (playlist[0] == it) {
                            mainNavController.navigate(ScreenDestination.Like(it.id))
                        } else {
                            mainNavController.navigate(ScreenDestination.Playlist(it.id))
                        }
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