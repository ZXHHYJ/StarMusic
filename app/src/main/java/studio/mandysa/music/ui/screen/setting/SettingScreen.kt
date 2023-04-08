package studio.mandysa.music.ui.screen.setting

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.TextFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.TopAppBar
import studio.mandysa.music.ui.common.bindTopAppBarState
import studio.mandysa.music.ui.common.rememberTopAppBarState
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.screen.ScreenDestination


@Composable
fun SettingScreen(
    mainNavController: NavController<ScreenDestination>,
    sheetNavController: NavController<BottomSheetDestination>,
    padding: PaddingValues,
) {
    val context = LocalContext.current
    val topAppBarState = rememberTopAppBarState()
    LazyColumn(
        modifier = Modifier
            .bindTopAppBarState(topAppBarState)
            .fillMaxSize()
            .padding(padding)
    ) {
        item {
            SettingItem(
                imageVector = Icons.Rounded.ColorLens,
                title = stringResource(id = R.string.theme),
                subTitle = stringResource(id = R.string.theme_info)
            ) {
                mainNavController.navigate(ScreenDestination.Theme)
            }
        }
        item {
            SettingItem(
                imageVector = Icons.Rounded.TextFormat,
                title = stringResource(id = R.string.lyric),
                subTitle = stringResource(id = R.string.lyric_info)
            ) {

            }
        }
        item {
            SettingItem(
                imageVector = Icons.Rounded.Info,
                title = stringResource(id = R.string.about),
                subTitle = stringResource(
                    id = R.string.about_info,
                    formatArgs = arrayOf(getVersionName(context))
                )
            ) {
                mainNavController.navigate(ScreenDestination.About)
            }
        }
    }
    TopAppBar(
        state = topAppBarState,
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(id = R.string.setting)
    )
}

private fun getVersionName(context: Context): String {
    try {
        return context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: Exception) {
        throw e
    }
}