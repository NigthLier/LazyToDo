package com.example.todoapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items WHERE userId = :userId")
    fun getAllTodos(userId: String): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_items WHERE id = :id AND userId = :userId")
    fun getTodoById(userId: String, id: Int): Flow<TodoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoItem): Long

    @Delete
    suspend fun deleteTodo(todo: TodoItem)

    @Update
    suspend fun updateTodo(todo: TodoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTodos(vararg todos: TodoItem)

    @Query("DELETE FROM todo_items WHERE userId = :userId")
    suspend fun deleteAllTodos(userId: String)
}