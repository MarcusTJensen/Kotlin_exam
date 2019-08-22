package com.example.testdb

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.support.annotation.WorkerThread

@Dao
public interface FootballerDAO {

    @Query("SELECT * FROM footballer_table")
    fun getAllFootballers(): List<Footballer>

    @Insert
    fun insert(footballer: Footballer)

    @Query("DELETE FROM footballer_table")
    fun deleteAll()

}