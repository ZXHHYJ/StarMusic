package studio.mandysa.music.ui.common

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.renderscript.Toolkit
import kotlin.math.abs

@Composable
fun MotionBlur(modifier: Modifier, url: String, paused: Boolean) {
    val context = LocalContext.current
    var imageBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    LaunchedEffect(url, url.isNotEmpty()) {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        val bitmap = imageLoader.execute(request).drawable?.toBitmap()
        if (bitmap != null) {
            imageBitmap = handleImageBlur(bitmap).asImageBitmap()
        }
    }

    var dstSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    var canvasSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    var scale by remember {
        mutableStateOf(1f)
    }

    fun updateScale() {
        val newScale = (100..300).random().toFloat() / 100
        if (newScale == scale) {
            updateScale()
            return
        }
        scale = newScale
    }

    val durationMillis = 2000

    val scaleAnim by animateFloatAsState(
        targetValue = scale,
        animationSpec = TweenSpec(durationMillis = durationMillis)
    ) {
        updateScale()
    }

    var pivotOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }

    fun updateOffset() {
        val widthDiff = abs(dstSize.width - canvasSize.width * scaleAnim).toInt()
        val heightDiff = abs(dstSize.height - canvasSize.height * scaleAnim).toInt()
        val x = (0..widthDiff).random().toFloat()
        val y = (0..heightDiff).random().toFloat()
        pivotOffset = Offset(x, y)
    }

    val pivotOffsetAnim by animateOffsetAsState(
        targetValue = pivotOffset,
        animationSpec = TweenSpec(durationMillis = durationMillis)
    ) {
        updateOffset()
    }

    LaunchedEffect(imageBitmap, !paused) {
        updateScale()
        updateOffset()
    }
    Canvas(
        modifier = modifier.onSizeChanged {
            val max = listOf(it.width, it.height).maxOrNull() ?: return@onSizeChanged
            dstSize = IntSize(max, max)
            canvasSize = it
        }) {
        imageBitmap?.let {
            scale(scale = scaleAnim, pivot = pivotOffsetAnim) {
                drawImage(image = it, dstSize = dstSize)
            }
        }
    }
}

private fun handleImageBlur(image: Bitmap): Bitmap {
    var blurBitmap = image.copy(Bitmap.Config.ARGB_8888, true)
    blurBitmap = scaleBitmap(blurBitmap, blurBitmap.height * 150 / blurBitmap.width)
    blurBitmap = meshBitmap(blurBitmap)
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

private fun scaleBitmap(origin: Bitmap, newHeight: Int): Bitmap {
    val height = origin.height
    val width = origin.width
    val scaleWidth = 150f / width
    val scaleHeight = newHeight.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
}