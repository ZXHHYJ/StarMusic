package studio.mandysa.music.ui.common

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 组合式BottomSheetDialog
 * @param sheetModifier 仅影响BottomSheet布局,不影响mainContent布局
 * @param sheetElevation bottomSheet背景的阴影
 * @param sheetShape bottomSheet的视图shape
 * @param mainContent activity的contentView
 * @param sheetContent bottomSheet弹出来的contentView
 * @param topLeftIcon 内置的顶部左侧Icon
 * @param topCenterIcon 内置的顶部中心点的Icon
 * @param topRightIcon 内置的顶部右侧Icon
 */
@SuppressLint("ModifierParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MultiBottomSheetLayout(
    sheetModifier: Modifier = Modifier,
    sheetElevation: Dp = 0.dp,
    sheetShape: Shape = MaterialTheme.shapes.large,
    mainContent: @Composable (sheetScreen: (MultiBottomSheet.Intent) -> Unit) -> Unit,
    sheetContent: @Composable (arguments: Bundle?) -> Unit,
    topLeftIcon: (@Composable (onClosePressed: () -> Unit) -> Unit)? = null,
    topCenterIcon: (@Composable (onClosePressed: () -> Unit) -> Unit)? = null,
    topRightIcon: (@Composable (onClosePressed: () -> Unit) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    //记录BottomSheet
    var currentBottomSheet: MultiBottomSheet.Intent? by remember{
        mutableStateOf(null)
    }
    //关闭的时候需要置空
    if(scaffoldState.bottomSheetState.isCollapsed){
        currentBottomSheet = null
    }
    //执行关闭BottomSheet
    val closeSheet: () -> Unit = {
        scope.launch {
            scaffoldState.bottomSheetState.collapse()
        }
    }
    //展开BottomSheet
    val openSheet: (MultiBottomSheet.Intent) -> Unit = {
        scope.launch {
            currentBottomSheet = it
            scaffoldState.bottomSheetState.expand()
        }
    }

    BottomSheetScaffold(sheetPeekHeight = 0.dp, scaffoldState = scaffoldState,
        sheetElevation = sheetElevation,
        sheetShape = sheetShape,
        // 可以通过外部传入【Modifier.padding(top = xx.dp)】
        // 来设置BottomSheet弹出来的视图【距离】『屏幕顶部的距离』
        modifier = sheetModifier,
        sheetContent = {
            currentBottomSheet?.let { currentSheetIntent ->
                BottomSheetWithTopClose(
                    content = {
                        sheetContent(currentSheetIntent.arguments)
                    },
                    topLeftIcon = topLeftIcon,
                    topCenterIcon = topCenterIcon,
                    topRightIcon = topRightIcon,
                    onClosePressed = closeSheet
                )
            }
        }) { paddingValues ->
        Box(Modifier.padding(paddingValues)){
            mainContent(openSheet)
        }
    }
}

sealed class MultiBottomSheet {
    //支持传入Bundle,通过Bundle去解析数据,渲染新BottomSheet页面内容
    class Intent(val arguments:Bundle? = null):MultiBottomSheet()
}

@Composable
private fun BottomSheetWithTopClose(
    onClosePressed: () -> Unit,
    modifier: Modifier = Modifier,
    topLeftIcon: (@Composable (onClosePressed: () -> Unit) -> Unit)? = null,
    topCenterIcon: (@Composable (onClosePressed: () -> Unit) -> Unit)? = null,
    topRightIcon: (@Composable (onClosePressed: () -> Unit) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(modifier.fillMaxWidth()) {
        content()
        if(topLeftIcon != null){
            IconButton(
                onClick = onClosePressed,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .size(29.dp)
            ) {
                topLeftIcon(onClosePressed)
            }
        }
        if (topCenterIcon != null) {
            IconButton(
                onClick = onClosePressed,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .size(29.dp)
            ) {
                topCenterIcon(onClosePressed)
            }
        }
        if(topRightIcon != null){
            IconButton(
                onClick = onClosePressed,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(29.dp)
            ) {
                topRightIcon(onClosePressed)
            }
        }
    }
}