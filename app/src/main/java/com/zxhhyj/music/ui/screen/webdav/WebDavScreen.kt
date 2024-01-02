package com.zxhhyj.music.ui.screen.webdav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTab
import com.zxhhyj.ui.view.AppTabRow
import com.zxhhyj.ui.view.roundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.moveToTop
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.rememberNavController

@Composable
fun WebDavScreen(
    paddingValues: PaddingValues,
    sheetNavController: NavController<SheetDestination>
) {
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.webdav)) })
        }) {
        val tabs = WebDavMediaLibRepository.webDavSources.takeIf { SettingRepository.EnableWebDav }
        val navController = rememberNavController(startDestination = tabs?.getOrNull(0))
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            AppTabRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs?.forEach { destination ->
                    AppTab(
                        selected = destination === navController.backstack.entries.last().destination,
                        onClick = {
                            if (!navController.moveToTop {
                                    it == destination
                                }) {
                                navController.navigate(destination)
                            }
                        }) {
                        Text(text = destination.remark.takeIf { it.isNotEmpty() }
                            ?: stringResource(id = R.string.nothing))
                    }
                }
            }
            NavHost(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                controller = navController
            ) { source ->
                val list = MediaLibHelper.songs.filter {
                    val songBean = it as? SongBean.WebDav
                    songBean?.webDavSource == source
                }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    roundColumn {
                        itemsIndexed(list) { index, it ->
                            SongItem(sheetNavController = sheetNavController, songBean = it) {
                                PlayerManager.play(list, index)
                            }
                        }
                    }
                }
            }
        }
    }
}