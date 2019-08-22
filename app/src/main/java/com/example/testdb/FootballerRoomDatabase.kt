package com.example.testdb

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.util.Log
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch

@Database(entities = arrayOf(Footballer::class), version = 2)
public abstract class FootballerRoomDatabase: RoomDatabase() {

    abstract fun footballerDao(): FootballerDAO

    companion object {

        @Volatile
        private var INSTANCE: FootballerRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FootballerRoomDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FootballerRoomDatabase::class.java,
                    "Footballer_database"
                ).addCallback(FootballerDatabaseCallback(scope))
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return  instance
            }
        }
    }

    private class FootballerDatabaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback(){

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->

                scope.launch(Dispatchers.IO) {

                    populateDatabase(database.footballerDao())
                }
            }
        }

        fun populateDatabase(footballerDao: FootballerDAO){

            footballerDao.deleteAll()

            var footballer = Footballer("Tobias Christensen", "IK Start", "MID", 9)
            footballerDao.insert(footballer)
            footballer = Footballer("Espen Børufsen", "IK Start", "MID", 6)
            footballerDao.insert(footballer)
            footballer = Footballer("Kristján Finnbogason", "IK Start", "ANG", 9)
            footballerDao.insert(footballer)
            footballer = Footballer("Andreas Lie", "Aalesund FK", "MÅL", 6)
            footballerDao.insert(footballer)
            footballer = Footballer("Erikson Lima", "Aalesund FK", "MID", 9)
            footballerDao.insert(footballer)
            footballer = Footballer("Niklas Castro", "Aalesund FK", "ANG", 11)
            footballerDao.insert(footballer)
            footballer = Footballer("Dadi Dodou Gaye", "KFUM Oslo", "FOR", 4)
            footballerDao.insert(footballer)
            footballer = Footballer("Kristoffer Hagen", "KFUM Oslo", "MID", 5)
            footballerDao.insert(footballer)
            footballer = Footballer("Juba Massinissa Moula", "KFUM Oslo", "ANG", 7)
            footballerDao.insert(footballer)
            footballer = Footballer("Johannes Grødtlien", "Strømmen IF", "FOR", 4)
            footballerDao.insert(footballer)
            footballer = Footballer("Marius Wennersteen Berntzen", "Strømmen IF", "Mål", 4)
            footballerDao.insert(footballer)
            footballer = Footballer("Sivert Gussiås", "Strømmen IF", "ANG", 8)
            footballerDao.insert(footballer)
            footballer = Footballer("Adrian Jonuzi Moller", "Skeid", "MÅL", 6)
            footballerDao.insert(footballer)
            footballer = Footballer("Matarr Kah", "Skeid", "FOR", 7)
            footballerDao.insert(footballer)
            footballer = Footballer("Mustafa Ahmed Hassan", "Skeid", "ANG", 10)
            footballerDao.insert(footballer)
            footballer = Footballer("Torje Wichne", "FK Jerv", "FOR", 5)
            footballerDao.insert(footballer)
            footballer = Footballer("Thomas Zernichow", "FK Jerv", "MID", 10)
            footballerDao.insert(footballer)
            footballer = Footballer("Aram Khalili", "FK Jerv", "ANG", 7)
            footballerDao.insert(footballer)

        }
    }
}
