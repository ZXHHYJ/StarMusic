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
import studio.mandysa.music.ui.theme.isMedium
import studio.mandysa.music.ui.theme.textColor

@Composable
fun ContentColumnItem(
    dialogNavController: NavController<DialogDestination>,
    coverUrl: String?,
    title: String?,
    message: String?,
    toolbarScope: @Composable () -> Unit
) {
    @Composable
    fun CoverItem() {
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
    }

    @Composable
    fun InfoItem(textAlign: TextAlign = TextAlign.Center) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalMargin)
                .placeholder(title == null),
            text = title ?: "",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            textAlign = textAlign,
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
            textAlign = textAlign,
            color = Color.Gray,
            overflow = TextOverflow.Ellipsis,
            maxLines = 5
        )
    }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(10.dp))
        if (isMedium) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(horizontalMargin))
                CoverItem()
                Column(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .weight(1.0f)
                ) {
                    InfoItem(TextAlign.Left)
                }
                Box(
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Alignment.Bottom)
                ) {
                    toolbarScope.invoke()
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        } else {
            Spacer(modifier = Modifier.height(10.dp))
            CoverItem()
            Spacer(modifier = Modifier.height(20.dp))
            InfoItem()
            Spacer(modifier = Modifier.height(5.dp))
            toolbarScope.invoke()
        }
    }
}