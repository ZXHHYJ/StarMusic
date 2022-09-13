package studio.mandysa.music.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.mandysa.music.R
import studio.mandysa.music.logic.model.ArtistSubModel
import studio.mandysa.music.logic.model.SearchSingerModel
import studio.mandysa.music.ui.common.AppAsyncImage
import studio.mandysa.music.ui.theme.*

@Composable
fun SingerItem(model: SearchSingerModel, onClick: () -> Unit) {
    SingerItem(
        picUrl = model.picUrl,
        name = model.name,
        hint = "${stringResource(id = R.string.album)}:${model.albumSize}"
    ) {
        onClick.invoke()
    }
}

@Composable
fun SingerItem(model: ArtistSubModel, onClick: () -> Unit) {
    SingerItem(
        picUrl = model.picUrl,
        name = model.nickname,
        hint = "${stringResource(id = R.string.album)}:${model.albumSize}"
    ) {
        onClick.invoke()
    }
}

@Composable
fun SingerItem(picUrl: String, name: String, hint: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = horizontalMargin, vertical = verticalMargin)
                .size(50.dp), contentAlignment = Alignment.Center
        ) {
            AppAsyncImage(modifier = Modifier.size(50.dp), cornerSize = 25.dp, url = picUrl)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(vertical = verticalMargin),
        ) {
            Text(
                text = name,
                color = textColor,
                fontSize = 15.sp, maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = hint,
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