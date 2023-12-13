package com.zxhhyj.music.ui.screen.search

import android.view.MotionEvent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.common.BoxWithPercentages
import com.zxhhyj.music.ui.item.AlbumItem
import com.zxhhyj.music.ui.item.ArtistItem
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.music.ui.theme.vertical
import com.zxhhyj.ui.theme.LocalColorScheme
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTab
import com.zxhhyj.ui.view.AppTabRow
import com.zxhhyj.ui.view.AppTextField
import com.zxhhyj.ui.view.RoundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.moveToTop
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.rememberNavController

enum class SearchScreenTabs {
    Single, Album, Singer
}

val SearchScreenTabs.tabName: String
    @Composable get() = when (this) {
        SearchScreenTabs.Single -> stringResource(id = R.string.single)
        SearchScreenTabs.Album -> stringResource(id = R.string.album)
        SearchScreenTabs.Singer -> stringResource(id = R.string.singer)
    }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<SheetDestination>,
    paddingValues: PaddingValues,
    start: SearchScreenTabs,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var searchKey by rememberSaveable {
        mutableStateOf("")
    }
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(paddingValues),
        topBar = { AppCenterTopBar(title = { Text(text = stringResource(id = R.string.search)) }) })
    {
        Column(modifier = Modifier.fillMaxSize()) {
            CompositionLocalProvider(
                LocalColorScheme provides LocalColorScheme.current.copy(
                    background = LocalColorScheme.current.highBackground
                )
            ) {
                AppTextField(
                    value = searchKey,
                    onValueChange = {
                        searchKey = it
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.input_key))
                    },
                    keyboardActions = KeyboardActions(onSearch = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontal),
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                    }, trailingIcon = {
                        if (searchKey.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    searchKey = ""
                                })
                        }
                    }
                )
            }
            val navController = rememberNavController(startDestination = start)
            AppTabRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SearchScreenTabs.entries
                    .forEach { destination ->
                        AppTab(
                            selected = destination == navController.backstack.entries.last().destination,
                            onClick = {
                                if (!navController.moveToTop {
                                        it == destination
                                    }) {
                                    navController.navigate(destination)
                                }
                            }
                        ) {
                            Text(text = destination.tabName)
                        }
                    }
            }

            RoundColumn(modifier = Modifier.fillMaxWidth()) {
                NavHost(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInteropFilter { event ->
                            if (event.action == MotionEvent.ACTION_DOWN) {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                            false
                        }, controller = navController
                ) { it ->
                    when (it) {
                        SearchScreenTabs.Single -> {
                            val songs =
                                (AndroidMediaLibRepository.songs + WebDavMediaLibRepository.songs).filter {
                                    it.songName.contains(searchKey) || it.artist.name.contains(
                                        searchKey
                                    )
                                }
                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                itemsIndexed(songs) { index, item ->
                                    SongItem(
                                        sheetNavController = sheetNavController,
                                        songBean = item
                                    ) {
                                        PlayerManager.play(songs, index)
                                    }
                                }
                            }
                        }

                        SearchScreenTabs.Album -> {
                            BoxWithPercentages(modifier = Modifier.fillMaxWidth()) {
                                var fixedCount = (maxWidth / 180.dp).toInt()
                                if (fixedCount < 2) {
                                    fixedCount = 2
                                }
                                val albums = MediaLibHelper.albums.filter {
                                    it.name.contains(searchKey)
                                }
                                LazyVerticalGrid(
                                    modifier = Modifier.fillMaxWidth(),
                                    columns = GridCells.Fixed(fixedCount),
                                ) {
                                    itemsIndexed(albums) { index, item ->
                                        val column = index % fixedCount
                                        val row: Int = index / fixedCount
                                        val p =
                                            (2 * horizontal + (fixedCount - 1) * horizontal) * 1f / fixedCount
                                        val left = horizontal + column * (horizontal - p)
                                        val right = p - left
                                        AlbumItem(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(
                                                    PaddingValues.Absolute(
                                                        top = if (row == 0) vertical else vertical / 2,
                                                        left = left,
                                                        right = right
                                                    )
                                                ), album = item
                                        ) {
                                            mainNavController.navigate(
                                                ScreenDestination.AlbumCnt(
                                                    item
                                                )
                                            )
                                        }
                                    }
                                }

                            }
                        }

                        SearchScreenTabs.Singer -> {
                            val artists = MediaLibHelper.artists.filter {
                                it.name.contains(searchKey)
                            }
                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                items(artists) {
                                    ArtistItem(artist = it) {
                                        mainNavController.navigate(
                                            ScreenDestination.SingerCnt(
                                                it
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}