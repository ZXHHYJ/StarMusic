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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import studio.mandysa.music.R
import studio.mandysa.music.ui.common.DialogCard
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.theme.horizontalMargin

@Composable
@Preview
fun MeMenu() {
    DialogCard {
        Column(modifier = Modifier.padding(horizontal = horizontalMargin)) {
            Spacer(modifier = Modifier.height(10.dp))
            MenuItem(
                title = stringResource(id = R.string.singers_i_follow),
                imageVector = Icons.Rounded.Favorite
            ) {

            }
            MenuItem(
                title = stringResource(id = R.string.sign_out),
                imageVector = Icons.Rounded.ExitToApp
            ) {

            }
        }
    }
}