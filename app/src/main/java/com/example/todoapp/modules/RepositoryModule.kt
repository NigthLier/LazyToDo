package com.example.todoapp.modules

import com.example.todoapp.data.dao.TodoDao
import com.example.todoapp.data.repository.TodoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideTodoRepository(todoDao: TodoDao,
                              firebaseDatabase: FirebaseDatabase,
                              firebaseAuth: FirebaseAuth
    ): TodoRepository {
        return TodoRepository(todoDao, firebaseDatabase, firebaseAuth)
    }
}