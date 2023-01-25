package studio.mandysa.music.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import studio.mandysa.music.R
import studio.mandysa.music.ui.theme.defaultHorizontal
import studio.mandysa.music.ui.theme.isAndroidPad
import studio.mandysa.music.ui.theme.onBackground


@Composable
fun AppTopBar(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Row(modifier = modifier.padding(horizontal = defaultHorizontal)) {
        content.invoke()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopSearchBar(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    menu: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    SearchBar(modifier = modifier, click = onClick) {
        Icon(
            imageVector = if (isAndroidPad) Icons.Rounded.Search else Icons.Rounded.Menu,
            contentDescription = null,
            modifier = Modifier.clickable(enabled = !isAndroidPad) {
                coroutineScope.launch {
                    if (drawerState.isClosed) {
                        drawerState.open()
                    }
                }
            },
            tint = onBackground
        )
        Text(text = stringResource(id = R.string.search_hint))
        menu.invoke()
    }
}