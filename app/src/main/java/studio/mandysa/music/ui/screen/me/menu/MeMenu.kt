package studio.mandysa.music.ui.screen.me.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.popAll
import studio.mandysa.music.R
import studio.mandysa.music.logic.user.UserManager
import studio.mandysa.music.ui.common.DialogCard
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.screen.ScreenDestination
import studio.mandysa.music.ui.theme.horizontalMargin

@Composable
fun MeMenu(
    mainNavController: NavController<ScreenDestination>,
    dialogNavController: NavController<DialogDestination>
) {
    DialogCard {
        Column(modifier = Modifier.padding(horizontal = horizontalMargin)) {
            Spacer(modifier = Modifier.height(10.dp))
            MenuItem(
                title = stringResource(id = R.string.singers_i_follow),
                imageVector = Icons.Rounded.Favorite
            ) {
                dialogNavController.pop()
                mainNavController.navigate(ScreenDestination.ArtistSub)
            }
            MenuItem(
                title = stringResource(id = R.string.sign_out),
                imageVector = Icons.Rounded.ExitToApp
            ) {
                dialogNavController.popAll()
                mainNavController.popAll()
                UserManager.signOut()
            }
        }
    }
}