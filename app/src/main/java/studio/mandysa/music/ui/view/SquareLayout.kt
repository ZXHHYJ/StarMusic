package studio.mandysa.music.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * @author Huang hao
 */
class SquareLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // For simple implementation, or internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view. We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        var mWidthMeasureSpec = widthMeasureSpec
        var mHeightMeasureSpec = heightMeasureSpec
        setMeasuredDimension(
            getDefaultSize(0, mWidthMeasureSpec),
            getDefaultSize(0, mHeightMeasureSpec)
        )
        // Children are just made to fill our space.
        val childWidthSize = measuredWidth
        //设置高度与宽度一样
        mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY)
        mHeightMeasureSpec = mWidthMeasureSpec
        super.onMeasure(mWidthMeasureSpec, mHeightMeasureSpec)
    }
}