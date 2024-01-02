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
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.repository.AndroidMediaLibRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.logic.utils.MediaLibHelper
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.item.AlbumItem
import com.zxhhyj.music.ui.item.ArtistItem
import com.zxhhyj.music.ui.item.SongItem
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.music.ui.screen.SheetDestination
import com.zxhhyj.music.ui.theme.horizontal
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTab
import com.zxhhyj.ui.view.AppTabRow
import com.zxhhyj.ui.view.AppTextField
import com.zxhhyj.ui.view.roundColumn
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
            .statusBarsPadding(),
        topBar = { AppCenterTopBar(title = { Text(text = stringResource(id = R.string.search)) }) }) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTextField(value = searchKey,
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
                },
                trailingIcon = {
                    if (searchKey.isNotEmpty()) {
                        Icon(imageVector = Icons.Rounded.Clear,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                searchKey = ""
                            })
                    }
                })
            val navController = rememberNavController(startDestination = start)
            AppTabRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SearchScreenTabs.entries.forEach { destination ->
                    AppTab(selected = destination == navController.backstack.entries.last().destination,
                        onClick = {
                            if (!navController.moveToTop {
                                    it == destination
                                }) {
                                navController.navigate(destination)
                            }
                        }) {
                        Text(text = destination.tabName)
                    }
                }
            }
            NavHost(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .pointerInteropFilter { event ->
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                        false
                    }, controller = navController
            ) { it ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), contentPadding = paddingValues
                ) {
                    roundColumn {
                        when (it) {
                            SearchScreenTabs.Single -> {
                                val songs =
                                    (AndroidMediaLibRepository.songs + WebDavMediaLibRepository.songs).filter {
                                        it.songName.contains(searchKey) || it.artistName?.contains(
                                            searchKey
                                        ) ?: false
                                    }
                                itemsIndexed(songs) { index, item ->
                                    SongItem(
                                        sheetNavController = sheetNavController, songBean = item
                                    ) {
                                        PlayerManager.play(songs, index)
                                    }
                                }

                            }

                            SearchScreenTabs.Album -> {
                                val albums = MediaLibHelper.albums.filterNotNull().filter {
                                    it.contains(searchKey)
                                }

                                items(albums) { albumName ->
                                    AlbumItem(
                                        albumName = albumName
                                    ) {
                                        mainNavController.navigate(
                                            ScreenDestination.AlbumCnt(
                                                albumName
                                            )
                                        )
                                    }
                                }

                            }

                            SearchScreenTabs.Singer -> {
                                val artists = MediaLibHelper.artists.filterNotNull().filter {
                                    it.contains(searchKey)
                                }
                                items(artists) {
                                    ArtistItem(artistName = it) {
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