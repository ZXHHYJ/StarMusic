package studio.mandysa.music.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import coil.load
import com.flaviofaria.kenburnsview.KenBurnsView
import com.google.android.renderscript.Toolkit
import studio.mandysa.music.R

class KenBurnsView : KenBurnsView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    fun load(url: String) {
        load(url) {}
    }

    override fun setImageDrawable(drawable: Drawable?) {
        drawable ?: return
        if (drawable is BitmapDrawable) {
            val blurBitmap =
                handleImageBlur(
                    context,
                    drawable.bitmap
                )
            super.setImageDrawable(BitmapDrawable(resources, blurBitmap))
        }
    }

    private fun handleImageBlur(context: Context, image: Bitmap): Bitmap {
        var blurBitmap = drawCover(
            image,
            context.getColor(R.color.translucent_black)
        )
        blurBitmap = scaleBitmap(blurBitmap, blurBitmap.height * 150 / blurBitmap.width)
        blurBitmap = meshBitmap(blurBitmap, floats)
        blurBitmap = Toolkit.blur(blurBitmap, 25)
        return blurBitmap
    }

    private val floats = floatArrayOf(
        -0.2351f,
        -0.0967f,
        0.2135f,
        -0.1414f,
        0.9221f,
        -0.0908f,
        0.9221f,
        -0.0685f,
        1.3027f,
        0.0253f,
        1.2351f,
        0.1786f,
        -0.3768f,
        0.1851f,
        0.2f,
        0.2f,
        0.6615f,
        0.3146f,
        0.9543f,
        0.0f,
        0.6969f,
        0.1911f,
        1.0f,
        0.2f,
        0.0f,
        0.4f,
        0.2f,
        0.4f,
        0.0776f,
        0.2318f,
        0.6f,
        0.4f,
        0.6615f,
        0.3851f,
        1.0f,
        0.4f,
        0.0f,
        0.6f,
        0.1291f,
        0.6f,
        0.4f,
        0.6f,
        0.4f,
        0.4304f,
        0.4264f,
        0.5792f,
        1.2029f,
        0.8188f,
        -0.1192f,
        1.0f,
        0.6f,
        0.8f,
        0.4264f,
        0.8104f,
        0.6f,
        0.8f,
        0.8f,
        0.8f,
        1.0f,
        0.8f,
        0.0f,
        1.0f,
        0.0776f,
        1.0283f,
        0.4f,
        1.0f,
        0.6f,
        1.0f,
        0.8f,
        1.0f,
        1.1868f,
        1.0283f
    )

    private fun meshBitmap(old: Bitmap, floats: FloatArray): Bitmap {
        val fArr2 = FloatArray(72)
        var i = 0
        while (i <= 5) {
            var i2 = 0
            var i3 = 5
            while (i2 <= i3) {
                val i4 = i * 12 + i2 * 2
                val i5 = i4 + 1
                fArr2[i4] = floats[i4] * old.width.toFloat()
                fArr2[i5] = floats[i5] * old.height.toFloat()
                i2++
                i3 = 5
            }
            i++
        }
        val newBit = Bitmap.createBitmap(old)
        val canvas = Canvas(newBit)
        canvas.drawBitmapMesh(newBit, 5, 5, fArr2, 0, null, 0, null)
        return newBit
    }

    private fun scaleBitmap(origin: Bitmap, newHeight: Int): Bitmap {
        val height = origin.height
        val width = origin.width
        val scaleWidth = 150f / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)//使用后乘
        return Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
    }

    /**
     * bitmap添加蒙层
     * @param old 原始图像
     * @param color 蒙层颜色
     * @return 新bitmap
     */
    private fun drawCover(old: Bitmap, color: Int): Bitmap {
        val newBit = old.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(newBit)
        canvas.drawColor(color)
        return newBit
    }

}