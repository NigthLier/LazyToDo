package com.example.todoapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.model.TodoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItemRow(todoItem: TodoItem,
                onChange: (TodoItem) -> Unit,
                onDelete: (TodoItem) -> Unit,
                onClick: (TodoItem) -> Unit,
                modifier: Modifier = Modifier) {

    val todo by rememberUpdatedState(todoItem)
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onChange(todo.copy(completed = !todo.completed))
                    false
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete(todo)
                    true
                }
                SwipeToDismissBoxValue.Settled -> false
            }
        },
        positionalThreshold = { 100.dp.value }
    )
    SwipeToDismissBox(
        modifier = modifier.padding(vertical = 4.dp),
        state = swipeState,
        backgroundContent = { SwipeBackground(swipeState) },
        content = { TodoItem(todo, onClick) }
    )
}