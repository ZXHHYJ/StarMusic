package studio.mandysa.music.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import studio.mandysa.music.R
import studio.mandysa.music.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuSearchBar(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    SearchBar(modifier = modifier, click = onClick) {
        Icon(
            imageVector = if (tabletMode) Icons.Rounded.Search else Icons.Rounded.Menu,
            contentDescription = null,
            modifier = Modifier.clickable(enabled = !tabletMode) {
                coroutineScope.launch {
                    if (drawerState.isClosed) {
                        drawerState.open()
                    }
                }
            },
            tint = onBackground
        )
        Text(
            text = stringResource(
                id = R.string.in_n_search,
                stringResource(id = R.string.media_lib)
            )
        )

    }
}

@Composable
fun TitleSearchBar(title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconSearchBar(modifier = modifier, onClick = onClick) {
        Text(text = stringResource(id = R.string.in_n_search, title))
    }
}

@Composable
fun IconSearchBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    scope: @Composable () -> Unit
) {
    SearchBar(modifier = modifier, click = onClick) {
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null,
            tint = onBackground
        )
        scope.invoke()
    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    click: () -> Unit = {},
    rowScope: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = defaultHorizontal,
                bottom = defaultHorizontal,
                top = 4.dp,
                end = defaultVertical
            )
            .height(42.dp)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(onClick = click)
            .padding(horizontal = defaultHorizontal),
        horizontalArrangement = Arrangement.spacedBy(defaultHorizontal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        rowScope.invoke(this)
    }
}