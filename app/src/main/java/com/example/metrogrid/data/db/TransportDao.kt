package com.example.metrogrid.data.db

import androidx.compose.ui.geometry.Offset
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.metrogrid.data.api.model.Station


@Dao
interface TransportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(Stations: List<Station>)

    @Query("SELECT * FROM stations WHERE name LIKE '%' || :search_query || '%'")
    fun getStations(search_query: String?): PagingSource<Int, Station>

    @Query("SELECT * FROM stations LIMIT :limit OFFSET :offset")
    fun getPagedStations(limit: Int, offset: Int): List<Station>
    @Query("SELECT * FROM stations LIMIT 5")
    fun confirmStation(): List<Station>

}