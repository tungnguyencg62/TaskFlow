package online.futuret.taskflow.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import online.futuret.taskflow.model.Task
import online.futuret.taskflow.ui.screen.SwipeToDismissTaskItem
import kotlin.math.roundToInt

@Composable
fun ReorderableTaskList(
    tasks: List<Task>,
    onReorder: (List<Task>) -> Unit,
    onTaskClick: (Task) -> Unit,
    onTaskDelete: (Task) -> Unit,
    onOpenDetail: (Task) -> Unit,
    modifier: Modifier = Modifier // Thêm tham số này
) {
    var items by remember(tasks) { mutableStateOf(tasks) }
    var draggingId by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableFloatStateOf(0f) }
    var itemHeightPx by remember { mutableFloatStateOf(0f) }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = items, key = {it.id}){task ->
            val isDragging = task.id == draggingId
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(if (isDragging) 1f else 0f)
                    .graphicsLayer{
                        translationY = if (isDragging) dragOffset else 0f
                        shadowElevation = if (isDragging) 12f else 0f
                    }
                    .onGloballyPositioned {coordinates ->
                        if (itemHeightPx == 0f){
                            itemHeightPx = coordinates.size.height.toFloat()
                        }

                    }
            ) {
                SwipeToDismissTaskItem(
                    task = task,
                    onClick = {onTaskClick(task)},
                    onDelete = {onTaskDelete(task)},
                    onOpenDetail = {onOpenDetail(task)},
                    dragHandleModifier = Modifier
                        .pointerInput(task.id) {
                        detectDragGestures(
                            onDragStart = {draggingId = task.id},
                            onDragEnd = {
                                draggingId = null
                                dragOffset = 0f
                                onReorder(items)
                            },
                            onDragCancel = {
                                draggingId = null
                                dragOffset = 0f
                            },
                            onDrag = {change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount.y

                                if(itemHeightPx > 0f){
                                    val currentIndex = items.indexOfFirst { it.id == task.id }
                                    val moveBy = (dragOffset / itemHeightPx).roundToInt()
                                    val targetIndex = (currentIndex + moveBy).coerceIn(0, items.size - 1)
                                    if(targetIndex != currentIndex){
                                        items = items.toMutableList().apply {
                                             add(targetIndex, removeAt(currentIndex))
                                        }
                                        dragOffset -= (targetIndex - currentIndex) * itemHeightPx
                                    }
                                }
                            }
                        )
                    }
                )
            }
        }
    }
}
























