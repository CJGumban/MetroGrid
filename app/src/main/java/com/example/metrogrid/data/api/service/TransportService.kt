package com.example.metrogrid.data.api.service

import com.example.metrogrid.data.api.model.ArrivalResult
import com.example.metrogrid.data.api.model.DepartureResult
import com.example.metrogrid.data.api.model.Station
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TransportService {

    @GET("stops")
    suspend fun getAllStations(
        @Query("query") query: String,
        @Query("results") results: Int
    ): Response<List<Station>>

    @GET("stops/{id}/departures")
    suspend fun getDepartures(
      @Path("id") id : String,
    ): Response<DepartureResult>

    @GET("stops/{id}/arrivals")
    suspend fun getArrivals(
        @Path("id") id: String
    ): Response<ArrivalResult>
}