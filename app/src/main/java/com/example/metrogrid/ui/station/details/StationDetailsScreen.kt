package com.example.metrogrid.ui.station.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.compose.MetroGridTheme
import com.example.metrogrid.R
import com.example.metrogrid.data.api.model.Departure
import com.example.metrogrid.data.api.model.DepartureLocation
import com.example.metrogrid.data.api.model.DepartureResult
import com.example.metrogrid.data.api.model.Line
import com.example.metrogrid.data.api.model.Operator
import com.example.metrogrid.data.api.model.Products
import com.example.metrogrid.data.api.model.Remark
import com.example.metrogrid.data.api.model.Stop
import com.example.metrogrid.epochtToEEEddMMyyyy
import com.example.metrogrid.offsetDateTime
import com.example.metrogrid.ui.composable.ErrorDialog
import com.example.metrogrid.ui.composable.ErrorDialogPreview
import com.example.metrogrid.ui.composable.InternetDialog
import com.example.metrogrid.ui.composable.LoadingScreen
import ph.theorangeco.data.models.states.UiState
import ph.theorangeco.data.models.states.getData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationDetailsScreen(
    stationArrivalResult: UiState = UiState.Initial,
    stationDepartureResult: UiState = UiState.Initial,
    onRefresh: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    locationName: String = "",
    hasInternet: Boolean = true

) {
    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.results),
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 32.sp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(
                            painterResource(R.drawable.arrow_back_ios_24px),
                            contentDescription = "back icon"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onRefresh() }) {
                        Icon(
                            painterResource(R.drawable.refresh_24px),
                            contentDescription = "refresh icon"
                        )
                    }
                },

                )
        },

        ) { paddingValues ->

        AnimatedVisibility(!hasInternet,modifier = Modifier.zIndex(2f).padding(top = paddingValues.calculateTopPadding())) {
            InternetDialog()
        }
        when (stationDepartureResult) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding())
                        .background(Color.Black.copy(alpha = 0.3f)) // Transparent overlay
                        .pointerInput(Unit) {}

                        .zIndex(2f)
                ) {

                    LoadingScreen()
                }
            }

            is UiState.Success -> {
                val data by remember { mutableStateOf(stationDepartureResult.getData<DepartureResult>()) }
                Column(
                    modifier = Modifier
                        .zIndex(1f)
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row(
                        modifier = Modifier
                            .height(48.dp)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_train),
                            contentDescription = "train icon",
                            Modifier
                                .size(24.dp)
                                .padding(end = 4.dp)
                        )

                        Text(
                            text = locationName,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp)
                        )
                    }
                    Text(
                        text = "Updated: ${epochtToEEEddMMyyyy(data.realtimeDataUpdatedAt)}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.secondary)
                    )
                    LazyColumn {
                        items(data.departures) { departure ->
                            DepartureItem(departure)
                        }
                    }
                }
            }

            is UiState.Failed -> {
                ErrorDialog(
                    message = stationDepartureResult.msgString,
                    buttonText = "Dismiss",
                    onDismiss = {onBackClicked()}
                )
            }
        }


    }
}

@Composable
fun DepartureItem(
    departure: Departure
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Row {
                Icon(
                    painterResource(R.drawable.east_24px),
                    contentDescription = "directionIcon"
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = departure.direction ?: "TDB",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W600)
                )

            }
            Text(
                text = offsetDateTime(departure.plannedWhen) ?: "TBD",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W800)
            )
        }
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Row {
                Spacer(Modifier.width(4.dp))
                Text(
                    text = departure.stop.name ?: "TDB",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                )

            }
            if ((departure.delay ?: 0) > 0) {

                Text(
                    text = "Delayed",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colorScheme.inversePrimary
                    ), modifier = Modifier.background(
                        MaterialTheme.colorScheme.errorContainer, shape = CircleShape.copy(
                            CornerSize(4.dp)
                        )
                    ).padding(horizontal = 2.dp)
                )

            }
        }
    }
}

@Composable
@Preview
fun StationDetailsScreenPreview(

) {
    MetroGridTheme {
        StationDetailsScreen(
            stationDepartureResult = UiState.Success(previewDepartures),
            locationName = "Berlin"
        )
    }
}

val previewDepartures =
    DepartureResult(
        departures = listOf(
            Departure(
                tripId = "T1001",
                stop = Stop(
                    type = "stop",
                    id = "900012105",
                    name = "Berlin Central Station",
                    location = DepartureLocation(
                        type = "location",
                        id = "loc-001",
                        latitude = 52.525,
                        longitude = 13.369
                    ),
                    products = Products(
                        suburban = true,
                        subway = true,
                        tram = false,
                        bus = true,
                        ferry = false,
                        express = true,
                        regional = true
                    )
                ),
                whenTime = "2025-11-28T14:50:00+01:00",
                plannedWhen = "2025-11-28T14:48:00+01:00",
                prognosedWhen = "2025-11-28T14:52:00+01:00",
                delay = 2,
                platform = "6",
                plannedPlatform = "6",
                prognosedPlatform = "6",
                prognosisType = "forecast",
                direction = "Hamburg",
                provenance = "Berlin Süd",
                line = Line(
                    type = "line",
                    id = "L100",
                    fahrtNr = "ICE 123",
                    name = "ICE 123",
                    isPublic = true,
                    adminCode = "DB",
                    productName = "ICE",
                    mode = "train",
                    product = "express",
                    operator = Operator(
                        type = "operator",
                        id = "OP1",
                        name = "DB Fernverkehr"
                    )
                ),
                remarks = listOf(
                    Remark("info", "R001", "Train is expected to be on time."),
                    Remark("warning", "R002", "Platform may change.")
                ),
                origin = null,
                destination = Stop(
                    type = "stop",
                    id = "8002549",
                    name = "Hamburg Hbf",
                    location = DepartureLocation(latitude = 53.552, longitude = 10.006),
                    products = Products(true, true, false, true, false, true, true)
                ),
                cancelled = false,
                occupancy = "medium",
                currentTripPosition = null
            ),

            // SECOND ITEM
            Departure(
                tripId = "T1002",
                stop = Stop(
                    type = "stop",
                    id = "900042001",
                    name = "Berlin Alexanderplatz",
                    location = DepartureLocation(latitude = 52.5219, longitude = 13.4132),
                    products = Products(
                        suburban = true,
                        subway = true,
                        tram = true,
                        bus = true,
                        ferry = false,
                        express = false,
                        regional = true
                    )
                ),
                whenTime = "2025-11-28T15:10:00+01:00",
                plannedWhen = "2025-11-28T15:05:00+01:00",
                prognosedWhen = null,
                delay = 5,
                platform = "3",
                plannedPlatform = "3",
                prognosedPlatform = null,
                prognosisType = null,
                direction = "Frankfurt (Oder)",
                provenance = "Berlin Friedrichstraße",
                line = Line(
                    type = "line",
                    id = "RE1",
                    fahrtNr = "RE 3311",
                    name = "RE1",
                    isPublic = true,
                    adminCode = "ODEG",
                    productName = "Regional",
                    mode = "train",
                    product = "regional",
                    operator = Operator("operator", "OP2", "ODEG")
                ),
                remarks = listOf(Remark("info", "R003", "Shortened train formation.")),
                origin = null,
                destination = Stop(
                    type = "stop",
                    id = "8011461",
                    name = "Frankfurt (Oder)",
                    location = DepartureLocation(latitude = 52.347, longitude = 14.550),
                    products = Products(true, false, false, true, false, false, true)
                ),
                cancelled = false,
                occupancy = "low",
                currentTripPosition = null
            ),

            // THIRD ITEM
            Departure(
                tripId = "T1003",
                stop = Stop(
                    type = "stop",
                    id = "900012207",
                    name = "Berlin Südkreuz",
                    location = DepartureLocation(latitude = 52.4751, longitude = 13.3658),
                    products = Products(true, true, false, true, false, true, true)
                ),
                whenTime = "2025-11-28T15:30:00+01:00",
                plannedWhen = "2025-11-28T15:30:00+01:00",
                prognosedWhen = null,
                delay = 0,
                platform = "12",
                plannedPlatform = "12",
                prognosedPlatform = null,
                prognosisType = null,
                direction = "Dresden",
                provenance = "Berlin Hauptbahnhof",
                line = Line(
                    type = "line",
                    id = "IC45",
                    fahrtNr = "IC 2045",
                    name = "IC 2045",
                    isPublic = true,
                    adminCode = "DB",
                    productName = "IC",
                    mode = "train",
                    product = "express",
                    operator = Operator("operator", "OP3", "DB Intercity")
                ),
                remarks = emptyList(),
                origin = null,
                destination = Stop(
                    type = "stop",
                    id = "8010085",
                    name = "Dresden Hbf",
                    location = DepartureLocation(latitude = 51.040, longitude = 13.738),
                    products = Products(true, false, false, true, false, false, true)
                ),
                cancelled = false,
                occupancy = "high",
                currentTripPosition = null
            )
        ),
        realtimeDataUpdatedAt = 1764383078
    )

