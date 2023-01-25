package studio.mandysa.music.ui.screen.cloud.me.meplaylist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.logic.bean.UserPlaylistBean
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.AppCard
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.defaultHorizontal
import studio.mandysa.music.ui.theme.textColor

@Composable
fun CloudMePlaylistScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    cloudMePlaylistViewModel: CloudMePlaylistViewModel = viewModel(),
) {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PlaylistItem(userPlaylistBean: UserPlaylistBean) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AppCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .combinedClickable(onClick = {
                            mainNavController.navigate(ScreenDestination.CloudPlaylistCnt(userPlaylistBean.id))
                        }, onLongClick = {
                            dialogNavController.navigate(
                                DialogDestination.PlaylistMenu(
                                    userPlaylistBean.id
                                )
                            )
                        }),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppAsyncImage(
                        modifier = Modifier.size(100.dp),
                        url = userPlaylistBean.coverImgUrl
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Text(text = userPlaylistBean.name, fontSize = 15.sp, color = textColor)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = userPlaylistBean.nickname, fontSize = 14.sp, color = textColor)
                    }
                }
            }
        }
    }

    val list by cloudMePlaylistViewModel.meAllPlaylist.observeAsState()
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = defaultHorizontal),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        list?.let {
            itemsIndexed(it) { index, value ->
                //第一个是我喜欢的歌单
                if (index != 0) {
                    PlaylistItem(userPlaylistBean = value)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(paddingValues))
        }
    }
}