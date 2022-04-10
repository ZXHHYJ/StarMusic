package studio.mandysa.music.logic.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import studio.mandysa.music.R
import kotlin.math.roundToInt

class HorizontalAlbumItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val length =
            parent.resources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin)
        val bl = 2.5
        view.layoutParams.width =
            parent.resources.getDimensionPixelOffset(R.dimen.album_width)
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val childCount = parent.adapter!!.itemCount
        when (itemPosition) {
            childCount - 1 -> {
                outRect.left = (length / bl).roundToInt()
                outRect.right = length
            }
            0 -> {
                outRect.left = length
                outRect.right = (length / bl).roundToInt()
            }
            else -> {
                outRect.left = (length / bl).roundToInt()
                outRect.right = (length / bl).roundToInt()
            }
        }
    }
}