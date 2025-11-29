package com.example.metrogrid.ui.station.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metrogrid.LogHandler
import com.example.metrogrid.NetworkObserver
import com.example.metrogrid.data.repo.TransportRepo
import com.example.metrogrid.singleOrError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ph.theorangeco.data.models.states.UiState
import javax.inject.Inject

@HiltViewModel
class StationDetailsViewModel @Inject constructor(
    private val repo: TransportRepo,
    private val networkObserver: NetworkObserver,

): ViewModel() {
    private var _departureDetails: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    private var _arrivalDetails: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val departureDetails = _departureDetails.asStateFlow()
    val arrivalDetails = _arrivalDetails.asStateFlow()

    var job: Job? = null


    val hasInternet: StateFlow<Boolean> = networkObserver.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )
    fun getDepartureDetails(id: String){
        job?.cancel()
        job = viewModelScope.launch (Dispatchers.IO){
            _departureDetails.value = UiState.Loading
            repo.getStationDepartures(id).singleOrError({
                _departureDetails.value = UiState.Success(it)

            },
                { _departureDetails.value = UiState.Failed(it.message.toString()) }
            )
        }
    }

    fun getArrivalDetails(id: String){
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            _arrivalDetails.value = UiState.Loading
            repo.getStationArrivals(id).singleOrError({
                _arrivalDetails.value = UiState.Success(it)
            },{
                _arrivalDetails.value = UiState.Failed(it.message.toString())
            })
        }
    }
}