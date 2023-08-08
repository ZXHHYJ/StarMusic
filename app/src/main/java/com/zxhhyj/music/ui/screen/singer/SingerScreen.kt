package com.zxhhyj.music.ui.screen.singer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibsRepository
import com.zxhhyj.music.ui.common.stateprompt.StatePrompt
import com.zxhhyj.music.ui.item.ArtistItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.search.SearchScreenTabs
import com.zxhhyj.ui.view.Scaffold
import com.zxhhyj.ui.view.TopBar
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun SingerScreen(
    mainNavController: NavController<ScreenDestination>,
    padding: PaddingValues
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        topBar = {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.singer),
                actions = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            mainNavController.navigate(
                                ScreenDestination.Search(
                                    SearchScreenTabs.Singer
                                )
                            )
                        }
                    )
                })
        }) {
        StatePrompt(
            empty = AndroidMediaLibsRepository.artists.isEmpty(),
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(AndroidMediaLibsRepository.artists) {
                    ArtistItem(artist = it) {
                        mainNavController.navigate(ScreenDestination.SingerCnt(it))
                    }
                }
            }
        }
    }
}