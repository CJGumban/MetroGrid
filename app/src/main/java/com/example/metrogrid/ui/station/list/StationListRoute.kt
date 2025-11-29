package com.example.metrogrid.ui.station.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.metrogrid.LogHandler
import com.example.metrogrid.ui.station.details.StationDetails
import com.example.metrogrid.ui.station.details.StationDetailsRoute
import kotlinx.serialization.Serializable

@Serializable
object StationList

@Composable
fun StationListRoute(
    navBackStackEntry: NavBackStackEntry, navController: NavHostController){
    val viewModel = hiltViewModel<StationListViewModel>()
    val stationList by viewModel.stationList.collectAsState()
    val hasInternet by viewModel.hasInternet.collectAsState()
    val stations by remember { mutableStateOf(viewModel.stations)}
    LaunchedEffect(hasInternet) {
        LogHandler.d("has Internet Check: ${hasInternet}")
    }


    StationListScreen(
        stations= stations,
        onItemClick = {id,name->
            navController.navigate(StationDetails(
            id = id,
            locationName = name
        )) },
        onRefresh = {viewModel.refresh()},
        hasInternet = hasInternet)
    }


