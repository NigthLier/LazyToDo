package com.example.todoapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.ui.components.DetailedTodo
import com.example.todoapp.ui.components.ListTopBar
import com.example.todoapp.ui.components.SlidingFromTop
import com.example.todoapp.ui.components.TodoItemRow
import com.example.todoapp.ui.components.UserProfilePanel
import com.example.todoapp.ui.viewmodel.AuthViewModel
import com.example.todoapp.ui.viewmodel.TodoViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController,
               authViewModel: AuthViewModel = hiltViewModel(),
               todoViewModel: TodoViewModel = hiltViewModel()) {

    val todos by todoViewModel.todos.collectAsState(initial = emptyList())

    var showForm by remember { mutableStateOf(false) }
    var currentTodo by remember { mutableStateOf<TodoItem?>(null) }
    var showUserProfile by remember { mutableStateOf(false) }
    val user by authViewModel.user.collectAsState()

    Box {
        Scaffold(
            topBar = {
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp),
                    shadowElevation = 4.dp
                ) {
                    ListTopBar(onProfile = { showUserProfile = true })
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    showForm = true
                    currentTodo = null
                }) {
                    Icon(Icons.Rounded.Pets, contentDescription = "Add Todo")
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(todos, key = { it.id }) { todo ->
                    TodoItemRow(
                        todo, todoViewModel::saveTodo, todoViewModel::removeTodo,
                        onClick = { todoItem -> showForm = true; currentTodo = todoItem },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        }

        SlidingFromTop(
            visibility = showForm,
            onCancel = { showForm = false }
        ) {
            DetailedTodo(
                todo = currentTodo,
                onSave = todoViewModel::saveTodo,
                onCancel = { showForm = false }
            )
        }

        SlidingFromTop(
            visibility = showUserProfile,
            onCancel = { showUserProfile = false }
        ) {
            UserProfilePanel(user,
                {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                todoViewModel::loadAllTodosToFirebase, todoViewModel::loadAllTodosFromFirebase)
        }
    }
}