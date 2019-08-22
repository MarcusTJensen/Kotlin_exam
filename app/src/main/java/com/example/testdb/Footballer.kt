package com.example.testdb

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "footballer_table")
data class Footballer(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "team")
    val team: String,
    @ColumnInfo(name = "position")
    val position: String,
    @ColumnInfo(name = "price")
    val price: Int
)