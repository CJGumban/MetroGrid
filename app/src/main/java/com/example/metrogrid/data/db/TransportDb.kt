package com.example.metrogrid.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.metrogrid.data.api.model.Converters
import com.example.metrogrid.data.api.model.Station
import timber.log.Timber
import java.util.concurrent.Executors

@Database(entities = [
    Station::class
], version = 1)
@TypeConverters(Converters::class)
abstract class TransportDb: RoomDatabase() {
    abstract fun transportDao(): TransportDao

    companion object {
        private var INSTANCE: TransportDb? = null

        fun getInstance(context: Context): TransportDb{
            if(INSTANCE == null){
                synchronized(TransportDb:: class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TransportDb::class.java, "transport-db.db")
                        .fallbackToDestructiveMigration(true)
                        .setQueryCallback({sqlQuery, bindArgs ->
                            Timber.tag("ROOM_QUERY SQL").d("ROOM_QUERY SQL: $sqlQuery \nArgs: $bindArgs")
                        }, Executors.newSingleThreadExecutor())
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}