package com.example.todoapp.data.repository

import androidx.annotation.WorkerThread
import com.example.todoapp.data.dao.TodoDao
import com.example.todoapp.data.model.TodoItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TodoRepository(private val todoDao: TodoDao,
                     private val firebaseDatabase: FirebaseDatabase,
                     private val firebaseAuth: FirebaseAuth) {

    private val _userId = MutableStateFlow(firebaseAuth.currentUser?.uid ?: "anonymous")
    private val userId: StateFlow<String> = _userId.asStateFlow()

    private val _todosRef = MutableStateFlow(
        firebaseDatabase.reference.child(firebaseAuth.currentUser?.uid ?: "anonymous"))
    private val todosRef: StateFlow<DatabaseReference> = _todosRef.asStateFlow()

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _userId.value = auth.currentUser?.uid ?: "anonymous"
            _todosRef.value = firebaseDatabase.reference.child(auth.currentUser?.uid ?: "anonymous")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllTodos(): Flow<List<TodoItem>> = userId.flatMapLatest { uid ->
        todoDao.getAllTodos(uid)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTodoById(id: Int): Flow<TodoItem> = userId.flatMapLatest { uid ->
        todoDao.getTodoById(uid, id)
    }

    @WorkerThread
    suspend fun insert(todo: TodoItem) {
        todoDao.insertTodo(todo.copy(userId = userId.value))
    }

    @WorkerThread
    suspend fun update(todo: TodoItem) {
        todoDao.updateTodo(todo.copy(userId = userId.value))
    }

    @WorkerThread
    suspend fun delete(todo: TodoItem) {
        todoDao.deleteTodo(todo.copy(userId = userId.value))
    }

    suspend fun loadAllTodosToFirebase() = withContext(Dispatchers.IO) {
        val localTodos = todoDao.getAllTodos(userId.value).first()
        val ref = todosRef.first()
        ref.removeValue()
        localTodos.forEach { todo ->
            ref.child(todo.id.toString()).setValue(todo)
        }
    }

    suspend fun loadAllTodosFromFirebase() = withContext(Dispatchers.IO) {
        val ref = todosRef.first()
        val snapshot = ref.get().await()
        val todos = snapshot.children.mapNotNull { it.getValue(TodoItem::class.java) }
        todoDao.deleteAllTodos(userId.value)
        todoDao.insertAllTodos(*todos.toTypedArray())
    }
}