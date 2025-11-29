package com.example.metrogrid.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.metrogrid.data.StationSearchRemoteMediator
import com.example.metrogrid.data.api.model.ArrivalResult
import com.example.metrogrid.data.api.model.DepartureResult
//import com.example.metrogrid.data.StationSearchPagingSource
import com.example.metrogrid.data.api.model.Station
import com.example.metrogrid.data.api.service.TransportService
import com.example.metrogrid.data.db.TransportDb
import com.example.metrogrid.process
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface ITransportRepo {
    suspend fun getAllStations(): Flow<List<Station>>

    @OptIn(ExperimentalPagingApi::class)
    fun getPagedStation(query: String): Flow<PagingData<Station>>

    fun getStationDepartures(id: String): Flow<DepartureResult>

    fun getStationArrivals(id: String): Flow<ArrivalResult>
}

@ActivityScoped
class TransportRepo @Inject constructor(
    private val TransportApi: TransportService,
    private val TransportDb: TransportDb
) : ITransportRepo {

    override fun getStationDepartures(id: String): Flow<DepartureResult> = flow {
        emit(TransportApi.getDepartures(id).process())
    }

    override fun getStationArrivals(id: String): Flow<ArrivalResult> = flow {
        emit(TransportApi.getArrivals(id).process())
    }

    override suspend fun getAllStations(): Flow<List<Station>> =
        flow {
            val data = TransportApi.getAllStations("a", 15).process()
            emit(data)
        }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedStation(query: String): Flow<PagingData<Station>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                initialLoadSize = 10
            ),
            initialKey = 1,
            remoteMediator = StationSearchRemoteMediator(
                transportDb = TransportDb,
                transportService = TransportApi
            )
        ) {
            TransportDb.transportDao().getStations("a")

        }.flow.flowOn(Dispatchers.IO)
    }


}
