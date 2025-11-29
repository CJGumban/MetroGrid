
package com.example.metrogrid.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.metrogrid.ApiError
import com.example.metrogrid.data.api.model.Station
import com.example.metrogrid.data.api.service.TransportService
import com.example.metrogrid.data.db.TransportDb
import com.example.metrogrid.process
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StationSearchRemoteMediator @Inject constructor(

    private val transportDb: TransportDb,
    private val transportService: TransportService,
) : RemoteMediator<Int, Station>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Station>
    ): MediatorResult {
        val dao = transportDb.transportDao()
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> { val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }
                    lastItem.id
                }
            }
            //using query to adjust the api from returning all of the data resulting to lag
            //endpoint doesn't have pagination
            val response = transportService.getAllStations("a",30).process()

            transportDb.withTransaction {
                dao.insertStations(response.filter { it.valid() })


            }
            MediatorResult.Success(
                endOfPaginationReached = true
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: ApiError){
            MediatorResult.Error(e)
        }

    }
}

