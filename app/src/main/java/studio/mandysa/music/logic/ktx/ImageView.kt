package studio.mandysa.music.logic.ktx

import android.widget.ImageView
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.RoundedCornersTransformation
import studio.mandysa.music.R

fun ImageView.load(url: String) {
    load(url) {
        placeholder(R.color.widget_bg_color_light)
        crossfade(true)
        size(ViewSizeResolver(this@load))
        transformations(RoundedCornersTransformation())
    }
}