package com.example.todoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {

    val todos: Flow<List<TodoItem>> = repository.getAllTodos()

    fun saveTodo(todo: TodoItem) = viewModelScope.launch {
        when(todo.id) {
            0 -> repository.insert(todo)
            else -> repository.update(todo)
        }
    }

    fun removeTodo(todo: TodoItem) = viewModelScope.launch {
        repository.delete(todo)
    }

    fun loadAllTodosToFirebase() = viewModelScope.launch {
        repository.loadAllTodosToFirebase()
    }

    fun loadAllTodosFromFirebase() = viewModelScope.launch {
        repository.loadAllTodosFromFirebase()
    }
}