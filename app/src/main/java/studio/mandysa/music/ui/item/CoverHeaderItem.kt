package studio.mandysa.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.material.placeholder
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.navigate
import studio.mandysa.music.ui.screen.DialogDestination
import studio.mandysa.music.ui.theme.cornerShape
import studio.mandysa.music.ui.theme.horizontalMargin
import studio.mandysa.music.ui.theme.textColor

@Composable
fun ItemCoverHeader(
    dialogNavController: NavController<DialogDestination>,
    coverUrl: String?,
    title: String?,
    message: String?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Card(
            modifier = Modifier.size(250.dp),
            shape = cornerShape,
            elevation = 10.dp
        ) {
            AsyncImage(
                model = coverUrl ?: "",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier
                .defaultMinSize(minWidth = 100.dp)
                .padding(horizontal = horizontalMargin)
                .placeholder(title == null),
            text = title ?: "",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = textColor
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalMargin)
                .clickable {
                    message?.let {
                        dialogNavController.navigate(DialogDestination.Message(it))
                    }
                }
                .placeholder(message == null),
            text = message ?: "",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            overflow = TextOverflow.Ellipsis,
            maxLines = 5
        )
        Spacer(modifier = Modifier.height(5.dp))
    }
}