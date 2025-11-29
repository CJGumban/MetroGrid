package com.example.metrogrid.ui.station.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.metrogrid.NetworkObserver
import com.example.metrogrid.data.repo.TransportRepo
import com.example.metrogrid.singleOrError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ph.theorangeco.data.models.states.UiState
import javax.inject.Inject

@HiltViewModel
class StationListViewModel @Inject constructor(
    private val repo: TransportRepo,
    private val networkObserver: NetworkObserver
) : ViewModel() {

    private var _stationList: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    var stationList = _stationList.asStateFlow()
    val _searchQuery = MutableStateFlow("")
    var job: Job? = null
    private var _networkAvailable: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    val hasInternet: StateFlow<Boolean> = networkObserver.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    fun loadStationList() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            _stationList.value = UiState.Loading
            repo.getAllStations().singleOrError(
                {
                    _stationList.value = UiState.Success(it)
                }, {
                    _stationList.value = UiState.Failed(it.message.toString())
                })
        }
    }

    fun refresh() {
        viewModelScope.launch{ _refreshTrigger.emit(Unit) }
    }

    //possible for searching
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val stations = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            _refreshTrigger
                .onStart {
                    emit(Unit)
                }.flatMapLatest {
                    repo.getPagedStation(query).cachedIn(viewModelScope)
                }
        }
}