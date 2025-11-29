package com.example.metrogrid.ui.station.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.toRoute
import com.example.metrogrid.LogHandler
import kotlinx.serialization.Serializable

@Serializable
data class StationDetails(
    val id: String,
    val locationName: String
)

@Composable
fun StationDetailsRoute(
    navBackStackEntry: NavBackStackEntry,
    navController: NavController,
){
    val viewModel = hiltViewModel<StationDetailsViewModel>()

    val stationArrivalResult by viewModel.arrivalDetails.collectAsState()
    val stationDepartureResult by viewModel.departureDetails.collectAsState()
    val args =  navBackStackEntry.toRoute<StationDetails>()
    val hasInternet by viewModel.hasInternet.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.getDepartureDetails(args.id)
    }

    StationDetailsScreen(
        stationArrivalResult = stationArrivalResult,
        stationDepartureResult = stationDepartureResult,
        onRefresh = { viewModel.getDepartureDetails(args.id) },
        locationName = args.locationName,
        onBackClicked = {navController.popBackStack()},
            hasInternet = hasInternet
    )
}