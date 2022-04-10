package studio.mandysa.music.logic.ktx

import android.widget.ImageView
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.RoundedCornersTransformation

fun ImageView.load(url: String) {
    load(url) {
        crossfade(true)
        size(ViewSizeResolver(this@load))
        transformations(RoundedCornersTransformation())
    }
}