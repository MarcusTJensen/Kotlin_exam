package com.example.testdb

import android.support.annotation.WorkerThread
import java.util.logging.Handler

class FootballerRepository(private val footballerDao: FootballerDAO) {

    @WorkerThread
    fun getAllFootballers(): List<Footballer> {

        return footballerDao.getAllFootballers()

    }

    @WorkerThread
    suspend fun insert(footballer: Footballer) {

        footballerDao.insert(footballer)
    }
}