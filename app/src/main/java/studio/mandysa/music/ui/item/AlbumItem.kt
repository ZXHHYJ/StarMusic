package studio.mandysa.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.service.playmanager.model.MetaAlbum
import studio.mandysa.music.ui.common.CardAsyncImage
import studio.mandysa.music.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

private fun stampToDate(s: String): String {
    val date = Date(s.toLong())
    return SimpleDateFormat.getDateInstance().format(date)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumItem(mateAlbum: MetaAlbum, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(70.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(horizontalMargin))
        Box(modifier = Modifier.size(50.dp)) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = 5.dp),
                colors = CardDefaults.cardColors(Color.LightGray),
                shape = RoundedCornerShape(25.dp)
            ) {}
            CardAsyncImage(size = 50.dp, url = mateAlbum.coverUrl)
        }
        Spacer(modifier = Modifier.width(horizontalMargin))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(vertical = verticalMargin),
        ) {
            Text(
                text = mateAlbum.name,
                color = textColor,
                fontSize = 15.sp, maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = stampToDate(mateAlbum.publishTime),
                color = textColorLight,
                fontSize = 13.sp, maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            tint = onBackground,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.padding(end = horizontalMargin))
    }
}