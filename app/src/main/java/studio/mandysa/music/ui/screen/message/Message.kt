package studio.mandysa.music.ui.screen.message

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import studio.mandysa.music.ui.common.MenuItem
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.theme.cornerShape
import studio.mandysa.music.ui.theme.dialogBackground
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Message(dialogNavController: NavController<DialogDestination>, message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = cornerShape,
        colors = CardDefaults.cardColors(containerColor = dialogBackground)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = horizontalMargin)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .verticalScroll(
                        state = rememberScrollState()
                    ),
                text = message,
                color = textColor
            )
            Spacer(modifier = Modifier.height(5.dp))
            val context = LocalContext.current
            MenuItem(
                title = stringResource(id = R.string.copy),
                imageVector = Icons.Rounded.ContentCopy
            ) {
                copy(context, message)
                dialogNavController.pop()
            }
        }
    }
}