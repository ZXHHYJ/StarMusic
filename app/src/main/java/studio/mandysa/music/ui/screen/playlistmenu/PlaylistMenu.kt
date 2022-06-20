package studio.mandysa.music.ui.screen.playlistmenu

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.olshevski.navigation.reimagined.NavController
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.cornerShape
import studio.mandysa.music.ui.theme.dialogBackground
import studio.mandysa.music.ui.theme.horizontalMargin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistMenu(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>,
    id: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = cornerShape,
        colors = CardDefaults.cardColors(containerColor = dialogBackground)
    ) {
        LazyColumn(modifier = Modifier.padding(horizontal = horizontalMargin)) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                MenuItem(
                    title = stringResource(id = R.string.remove_playlist),
                    imageVector = Icons.Rounded.Delete
                ) {

                }
            }
        }
    }
}