package studio.mandysa.music.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import studio.mandysa.music.R
import studio.mandysa.music.logic.utils.copyText
import studio.mandysa.music.ui.screen.BottomSheetDestination
import studio.mandysa.music.ui.theme.horizontal
import studio.mandysa.music.ui.theme.textColor

@Composable
fun MessageDialog(dialogNavController: NavController<BottomSheetDestination>, message: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = horizontal)
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
        MenuItem(
            title = stringResource(id = R.string.copy),
            imageVector = Icons.Rounded.ContentCopy
        ) {
            copyText(message)
            dialogNavController.pop()
        }
    }

}