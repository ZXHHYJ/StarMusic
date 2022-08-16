package studio.mandysa.music.ui.screen.message

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.logic.ktx.copy
import studio.mandysa.music.ui.common.AppDialog
import studio.mandysa.music.ui.common.AppMenuButton
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.textColor

@Composable
fun Message(dialogNavController: NavController<DialogDestination>, message: String) {
    AppDialog {
        Column(
            modifier = Modifier
                .padding(horizontal = horizontalMargin)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .verticalScroll(
                        state = rememberScrollState()
                    ),
                text = message,
                color = textColor
            )
            Spacer(modifier = Modifier.height(5.dp))
            val context = LocalContext.current
            AppMenuButton(
                title = stringResource(id = R.string.copy),
                icon = Icons.Rounded.ContentCopy
            ) {
                copy(context, message)
                dialogNavController.pop()
            }
        }
    }
}