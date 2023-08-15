package com.zxhhyj.music.ui.screen.singer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.zxhhyj.music.ui.item.ArtistItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.search.SearchScreenTabs
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppIconButton
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.RoundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate

@Composable
fun SingerScreen(
    mainNavController: NavController<ScreenDestination>,
    paddingValues: PaddingValues
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColorScheme.current.subBackground)
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = {
            AppCenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.singer),
                actions = {
                    AppIconButton(onClick = {
                        mainNavController.navigate(
                            ScreenDestination.Search(
                                SearchScreenTabs.Singer
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null
                        )
                    }
                })
        }) {
        RoundColumn(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(AndroidMediaLibsRepository.artists) {
                    ArtistItem(artist = it) {
                        mainNavController.navigate(ScreenDestination.SingerCnt(it))
                    }
                }
            }
        }
    }
}