package com.zxhhyj.music.ui.common


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.blue
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.graphics.scale
import coil.ImageLoader
import coil.request.ImageRequest
import com.flaviofaria.kenburnsview.KenBurnsView
import com.flaviofaria.kenburnsview.RandomTransitionGenerator
import com.google.android.renderscript.Toolkit
import com.zxhhyj.music.ui.common.lifecycle.rememberLifecycle

@Composable
fun MotionBlur(
    modifier: Modifier,
    whiteObscuration: Color = Color(0x20FFFFFF),
    blackObscuration: Color = Color(0x3C000000),
    url: String?,
    paused: Boolean
) {

    var drawable by remember {
        mutableStateOf<Drawable?>(null)
    }

    val context = LocalContext.current
    val imageLoader = ImageLoader(context)
    LaunchedEffect(url) {
        if (url == null) return@LaunchedEffect
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        val bitmap = imageLoader.execute(request).drawable?.toBitmap()
        if (bitmap != null) {
            drawable = handleImageBlur(
                whiteObscuration,
                blackObscuration,
                bitmap
            ).toDrawable(context.resources)
            bitmap.recycle()
        }
    }
    val lifecycle = rememberLifecycle()
    AndroidView(factory = {
        KenBurnsView(it).apply {
            setTransitionGenerator(
                RandomTransitionGenerator(
                    3000,
                    LinearInterpolator()
                )
            )
        }
    }, modifier = modifier) {
        if (drawable != null && it.drawable != drawable) {
            it.setImageDrawable(drawable!!)
        }
        if (paused) {
            it.resume()
        } else {
            it.resume()
        }
        lifecycle.onLifeResume {
            it.resume()
        }
        lifecycle.onLifePause {
            it.pause()
        }
    }
}

private fun handleImageBlur(
    whiteObscuration: Color,
    blackObscuration: Color,
    image: Bitmap
): Bitmap {
    var blurBitmap = image.copy(Bitmap.Config.ARGB_8888, true)
    val brightnessList = arrayListOf<Double>()
    blurBitmap.scale(3, 3).let {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val pixel = it.getPixel(i, j)
                val r = (0.299 * pixel.red) + (0.587 * pixel.green) + (0.114 * pixel.blue)
                brightnessList.add(r)
            }
        }
    }
    val average = calculateAverage(brightnessList)
    val median = calculateMedian(brightnessList)
    val canvas = Canvas(blurBitmap)
    if (average < 50 && median < 50) {
        canvas.drawColor(whiteObscuration.toArgb())
    } else {
        canvas.drawColor(blackObscuration.toArgb())
    }
    blurBitmap = blurBitmap.scale(150, 150)
    blurBitmap = meshBitmap(blurBitmap)
    blurBitmap = Toolkit.blur(blurBitmap, 25)
    return blurBitmap
}

//计算平均值
private fun calculateAverage(numbers: List<Double>): Double {
    if (numbers.isEmpty()) return 0.0
    return numbers.sum() / numbers.size
}

//计算中位数
private fun calculateMedian(numbers: List<Double>): Double {
    val sortedNumbers = numbers.sorted()
    val middleIndex = sortedNumbers.size / 2
    return if (sortedNumbers.size % 2 == 0) {
        (sortedNumbers[middleIndex - 1] + sortedNumbers[middleIndex]) / 2.0
    } else {
        sortedNumbers[middleIndex]
    }
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

private fun meshBitmap(old: Bitmap): Bitmap {
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