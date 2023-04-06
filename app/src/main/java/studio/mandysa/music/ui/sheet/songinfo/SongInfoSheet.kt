package studio.mandysa.music.ui.sheet.songinfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.popAll
import studio.mandysa.music.logic.ktx.toTime
import studio.mandysa.music.service.playmanager.bean.SongBean
import studio.mandysa.music.service.playmanager.ktx.*
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.common.AppCard
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontal
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.textColorLight
import studio.mandysa.music.ui.theme.vertical

/**
 * @author 黄浩
 */

@Composable
fun SongInfoSheet(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    song: SongBean,
) {
    LazyColumn {
        item {
            Box(
                modifier = Modifier.padding(
                    horizontal = horizontal,
                    vertical = vertical
                )
            ) {
                Row(
                    modifier = Modifier
                        .height(70.dp)
                ) {
                    AppCard(backgroundColor = Color.Transparent, modifier = Modifier.size(70.dp)) {
                        AppAsyncImage(modifier = Modifier.fillMaxSize(), url = song.coverUrl)
                    }
                    Column(modifier = Modifier.padding(vertical)) {
                        Text(
                            text = song.title,
                            color = textColor,
                            fontSize = 15.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.weight(1.0f))
                        Text(
                            text = song.artist.allArtist(),
                            color = textColorLight,
                            fontSize = 13.sp,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
        item {
            when (song) {
                is SongBean.Local -> SongInfoItem(
                    title = stringResource(id = studio.mandysa.music.R.string.duration),
                    info = song.duration.toTime()
                )
                is SongBean.Network -> {

                }
            }

        }
        item {
            SongInfoItem(
                title = stringResource(id = studio.mandysa.music.R.string.album),
                info = song.album.name,
                modifier = Modifier.clickable {
                    when (song) {
                        is SongBean.Local -> {
                            sheetNavController.popAll()
                            mainNavController.navigate(ScreenDestination.AlbumCnt(album = song.album))
                        }
                        is SongBean.Network -> {

                        }
                    }
                }
            )
        }
        item {
            when (song) {
                is SongBean.Local -> {
                    SongInfoItem(
                        title = stringResource(id = studio.mandysa.music.R.string.bit_rate),
                        info = ""
                    )
                    // TODO: 未完成比特率的获取
                }
                is SongBean.Network -> {

                }
            }
        }
        item {
            when (song) {
                is SongBean.Local -> {
                    val hz = song.size / (song.duration / 1000)
                    SongInfoItem(
                        title = stringResource(id = studio.mandysa.music.R.string.sample_rate),
                        info = "${getClosestValue(hz.toInt())} Hz"
                    )
                }
                is SongBean.Network -> {
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(vertical))
        }
        item {
            when (song) {
                is SongBean.Local -> {
                    SongInfoItem(
                        title = stringResource(id = studio.mandysa.music.R.string.file_path),
                        info = song.data
                    )
                }
                is SongBean.Network -> {
                }
            }
        }
        item {
            when (song) {
                is SongBean.Local -> {
                    SongInfoItem(
                        title = stringResource(id = studio.mandysa.music.R.string.file_size),
                        info = "${song.size / 1024 / 1024} MB"
                    )
                }
                is SongBean.Network -> {
                }
            }
        }
    }
}

private fun getClosestValue(targetNum: Int): Int {
    val commonSampleRates = listOf(8000, 11025, 22050, 32000, 44100, 47250, 48000)
    var left = 0
    var right: Int
    right = commonSampleRates.size - 1
    while (left != right) {
        val midIndex = (right + left) / 2
        val mid = right - left
        val midValue = commonSampleRates[midIndex]
        if (targetNum == midValue) {
            return midIndex
        }
        if (targetNum > midValue) {
            left = midIndex
        } else {
            right = midIndex
        }
        if (mid <= 2) {
            break
        }
    }
    return if ((commonSampleRates[right] - commonSampleRates[left]) / 2 > targetNum) commonSampleRates[right] else commonSampleRates[left]
}