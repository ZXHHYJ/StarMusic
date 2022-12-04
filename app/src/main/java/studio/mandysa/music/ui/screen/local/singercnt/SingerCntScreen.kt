package studio.mandysa.music.ui.screen.local.singercnt

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.onBackground

@Composable
fun SingerCntScreen(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    paddingValues: PaddingValues,
    artistId: Long
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.Transparent,
        contentColor = onBackground,
        elevation = 0.dp
    ) {
        IconButton(onClick = { mainNavController.pop() }) {
            Icon(Icons.Rounded.ArrowBack, null)
        }
    }
}