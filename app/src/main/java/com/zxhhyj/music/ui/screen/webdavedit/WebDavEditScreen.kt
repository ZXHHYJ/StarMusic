package com.zxhhyj.music.ui.screen.webdavedit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.WebDavSource
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.ui.screen.DialogDestination
import com.zxhhyj.music.ui.screen.ScreenDestination
import com.zxhhyj.ui.view.AppCenterTopBar
import com.zxhhyj.ui.view.AppScaffold
import com.zxhhyj.ui.view.AppTextField
import com.zxhhyj.ui.view.RoundColumn
import com.zxhhyj.ui.view.item.Item
import com.zxhhyj.ui.view.item.ItemArrowRight
import com.zxhhyj.ui.view.item.ItemSpacer
import com.zxhhyj.ui.view.item.ItemSubTitle
import com.zxhhyj.ui.view.roundColumn
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop


/**
 * 编辑[WebDavSource]的信息
 * @param indexInWebDavSourceList 在[com.zxhhyj.music.logic.repository.WebDavMediaLibRepository.webDavSources]中的下标[indexInWebDavSourceList]
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WebDavEditScreen(
    paddingValues: PaddingValues,
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    indexInWebDavSourceList: Int,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val webDavSource by remember(WebDavMediaLibRepository.webDavSources) {
        mutableStateOf(WebDavMediaLibRepository.webDavSources.getOrNull(indexInWebDavSourceList))
    }
    val copyWebDavSource by rememberSaveable {
        mutableStateOf(webDavSource)
    }
    var username by rememberSaveable {
        mutableStateOf(webDavSource?.username ?: "")
    }
    var password by rememberSaveable {
        mutableStateOf(webDavSource?.password ?: "")
    }
    var address by rememberSaveable {
        mutableStateOf(webDavSource?.address ?: "")
    }
    var remark by rememberSaveable {
        mutableStateOf(webDavSource?.remark ?: "")
    }
    LaunchedEffect(username, password, address, remark) {
        webDavSource?.let {
            WebDavMediaLibRepository.replace(
                indexInWebDavSourceList,
                WebDavSource(
                    username, password, address, remark, it.folders
                )
            )
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            if (copyWebDavSource?.username != username || copyWebDavSource?.password != password || copyWebDavSource?.address != address && webDavSource?.folders?.isNotEmpty() == true) {
                dialogNavController.navigate(DialogDestination.RefreshWebDavMediaLib)
            }
        }
    }
    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            AppCenterTopBar(title = { Text(text = stringResource(id = R.string.edit_webdav)) })
        }) {
        val scrollState = rememberLazyListState()
        if (scrollState.isScrollInProgress) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
            state = scrollState
        ) {
            item {
                ItemSpacer()
            }
            roundColumn {
                item {
                    AppTextField(
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = address,
                        onValueChange = {
                            address = it
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.address))
                        })
                }
            }
            item {
                ItemSpacer()
            }
            roundColumn {
                item {
                    AppTextField(
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = username,
                        onValueChange = {
                            username = it
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.user))
                        })
                }
            }
            item {
                ItemSpacer()
            }
            roundColumn {
                item {
                    AppTextField(
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.password))
                        })
                }
            }
            item {
                ItemSpacer()
            }
            roundColumn {
                item {
                    AppTextField(
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = remark,
                        onValueChange = {
                            remark = it
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.remark))
                        })
                }
            }
            item {
                ItemSubTitle {
                    Text(text = stringResource(id = R.string.folder_manager))
                }
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    ItemArrowRight(icon = {
                        Icon(
                            imageVector = Icons.Rounded.Folder, contentDescription = null
                        )
                    },
                        text = { Text(text = stringResource(id = R.string.folder_manager)) },
                        subText = { }) {
                        mainNavController.navigate(
                            ScreenDestination.WebDavFolderManager(
                                indexInWebDavSourceList,
                                address
                            )
                        )
                    }
                }
            }
            item {
                ItemSubTitle {
                    Text(text = stringResource(id = R.string.folder))
                }
            }
            roundColumn {
                webDavSource?.let {
                    items(it.folders) {
                        Item(
                            icon = {
                                Icon(
                                    imageVector = Icons.Rounded.Folder, contentDescription = null
                                )
                            },
                            text = { Text(text = it.replace(address, "").replace("/", "")) },
                            subText = { }) {}
                    }
                }
            }
            item {
                ItemSubTitle {
                    Text(text = stringResource(id = R.string.delete))
                }
            }
            item {
                RoundColumn(modifier = Modifier.fillMaxWidth()) {
                    Item(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.delete)) },
                        subText = { }
                    ) {
                        runCatching {
                            WebDavMediaLibRepository.deleteWebSource(indexInWebDavSourceList)
                        }
                        mainNavController.pop()
                    }
                }
            }
        }
    }
}