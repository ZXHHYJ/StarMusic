package studio.mandysa.music.ui.screen.me.meplaylist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import studio.mandysa.music.ui.common.CardAsyncImage
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.round

@Composable
fun MePlaylistScreen(
    navController: NavController,
    mePlaylist: MePlaylistViewModel = viewModel()
) {
    Column {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            elevation = 0.dp
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBack, null)
            }
        }
        Divider(thickness = 1.dp)
        val items by mePlaylist.playlist.collectAsState(listOf())
        LazyColumn {
            items(items) {
                PlaylistItem(
                    coverUrl = it.coverImgUrl,
                    title = it.name,
                    nickname = it.nickname,
                ) {
                    navController.navigate("playlist/${it.id}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistItem(coverUrl: String, title: String, nickname: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalMargin)
            .padding(top = 5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable(onClick = onClick), shape = RoundedCornerShape(round)
        ) {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                CardAsyncImage(size = 100.dp, roundSize = 0.dp, url = coverUrl)
                Spacer(modifier = Modifier.width(5.dp))
                Column {
                    Text(text = title, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = nickname, fontSize = 14.sp)
                }
            }
        }
    }
}