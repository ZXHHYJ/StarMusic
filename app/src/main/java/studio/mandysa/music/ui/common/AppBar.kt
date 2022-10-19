package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import studio.mandysa.music.ui.theme.background
import studio.mandysa.music.ui.theme.onBackground
import studio.mandysa.music.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
private val colors: TopAppBarColors
    @Composable get() = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = background,
        navigationIconContentColor = onBackground,
        titleContentColor = textColor,
        actionIconContentColor = onBackground
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMediumTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    //MediumTopAppBar(title, modifier, navigationIcon, actions, colors, scrollBehavior)
}