package com.example.testdb

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.CoroutineContext

class FootballerModel(application: Application): AndroidViewModel(application) {

    private var parentJob= Job()
    private val coroutineContext: kotlin.coroutines.experimental.CoroutineContext
        get()= parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repo: FootballerRepository

    val allFootballers: List<Footballer>

    init {
        val footballerDao = FootballerRoomDatabase.getDatabase(application, scope).footballerDao()
        repo = FootballerRepository(footballerDao)
        allFootballers = repo.getAllFootballers()
    }

    fun insert(footballer: Footballer) = scope.launch(Dispatchers.IO) {

        repo.insert(footballer)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}