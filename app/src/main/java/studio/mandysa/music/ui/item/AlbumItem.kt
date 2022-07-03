package studio.mandysa.music.ui.item

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.service.playmanager.model.MetaAlbum
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.theme.textColor
import studio.mandysa.music.ui.theme.textColorLight
import java.text.SimpleDateFormat
import java.util.*

private fun stampToDate(s: String): String {
    val date = Date(s.toLong())
    return SimpleDateFormat.getDateInstance().format(date)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumItem(mateAlbum: MetaAlbum, onClick: () -> Unit) {
    val size = 120.dp
    Column(
        modifier = Modifier.width(size)
    ) {
        Box(modifier = Modifier.size(size)) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = 5.dp),
                colors = CardDefaults.cardColors(Color.LightGray),
                shape = RoundedCornerShape(size)
            ) {}
            AppAsyncImage(size = size, url = mateAlbum.coverUrl, onClick = onClick)
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = mateAlbum.name,
            color = textColor,
            fontSize = 13.sp,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stampToDate(mateAlbum.publishTime),
            color = textColorLight,
            fontSize = 13.sp,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}