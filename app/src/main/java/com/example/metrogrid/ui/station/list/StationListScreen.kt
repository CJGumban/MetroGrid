package com.example.metrogrid.ui.station.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.compose.MetroGridTheme
import com.example.metrogrid.LogHandler
import com.example.metrogrid.R
import com.example.metrogrid.data.api.model.Location
import com.example.metrogrid.data.api.model.Station
import com.example.metrogrid.ui.composable.InternetDialog
import com.example.metrogrid.ui.composable.LoadingScreen
import com.example.metrogrid.ui.composable.SpacerLine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationListScreen(
    stations: Flow<PagingData<Station>> = MutableStateFlow(
        PagingData.empty(),

        ),
    onRefresh: () -> Unit = {},
    onItemClick: (id: String, name: String) -> Unit = {_,_->},
    hasInternet: Boolean = true
) {
    val lazyPagingItems = stations.collectAsLazyPagingItems()

    Scaffold(
       topBar = {
           TopAppBar(
               title = { Text("MetroGrid", style = MaterialTheme.typography.titleLarge.copy(fontSize = 32.sp)) },
               actions = { IconButton(onClick = {onRefresh()}) {
                   Icon(painterResource(R.drawable.refresh_24px), contentDescription = "refresh icon")
               }},

               )
       }
    ) { paddingValues ->
        AnimatedVisibility(!hasInternet,modifier = Modifier.zIndex(2f).padding(top = paddingValues.calculateTopPadding())) {
            InternetDialog()
        }
        if (lazyPagingItems.loadState.refresh == LoadState.Loading){
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


        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .zIndex(1f)
            ,

            ) {

            /*if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
*/
            items(count = lazyPagingItems.itemCount) { index ->
                val item = lazyPagingItems[index]
                if (item != null) {
                    LogHandler.d("stationItem(${item})")
                    StationItem(
                        item, onClick =
                            {

                                onItemClick(item.id.split(":")[2],item.name?:"N/A")
                            })
                }
            }
            if (lazyPagingItems.loadState.append == LoadState.Loading) {
                item() {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

}

@Composable
fun StationItem(item: Station, onClick: () -> Unit) {

    Row(

        modifier = Modifier
            .clickable {
                onClick()
            }
            .padding(top = 8.dp, start = 4.dp)
            .fillMaxWidth()
            .height(32.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text("${item.name} (${item.type})")


    }
    SpacerLine(.7.dp)
}


@Composable
@Preview(showSystemUi = false, showBackground = false, device = "id:pixel_5")
fun StationListScreenPreview() {
    MetroGridTheme {
        StationListScreen(
            stations= MutableStateFlow(
               PagingData.from(stationListPreviewData()),

        )
        )
    }
}

fun stationListPreviewData(): List<Station> {
    return listOf(
        Station(
            id = "de:11000:900160515",
            name = "Arendsweg",
            type = "station",
            weight = 2306.0,
            location = Location(
                latitude = 52.534882,
                longitude = 13.505564,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900193002",
            name = "S Adlershof",
            type = "station",
            weight = 17007.25,
            location = Location(
                latitude = 52.435102,
                longitude = 13.540553,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900140011",
            name = "Antonplatz",
            type = "station",
            weight = 6175.25,
            location = Location(
                latitude = 52.548283,
                longitude = 13.450855,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900110019",
            name = "Am Friedrichshain",
            type = "station",
            weight = 2806.5,
            location = Location(
                latitude = 52.528015,
                longitude = 13.425135,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900012105::2",
            name = "Abgeordnetenhaus",
            type = "station",
            weight = 924.25,
            location = Location(
                latitude = 52.506456,
                longitude = 13.379986,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900012105::1",
            name = "Abgeordnetenhaus",
            type = "station",
            weight = 908.0,
            location = Location(
                latitude = 52.506112,
                longitude = 13.380041,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900110004",
            name = "S Landsberger Allee",
            type = "station",
            weight = 16212.75,
            location = Location(
                latitude = 52.528772,
                longitude = 13.455944,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900150020",
            name = "Alt-Hohenschönhausen",
            type = "station",
            weight = 3501.25,
            location = Location(
                latitude = 52.548868,
                longitude = 13.50476,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900140005",
            name = "Albertinenstr.",
            type = "station",
            weight = 6175.25,
            location = Location(
                latitude = 52.549788,
                longitude = 13.457775,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900100003",
            name = "S+U Alexanderplatz",
            type = "station",
            weight = 13918.0,
            location = Location(
                latitude = 52.521512,
                longitude = 13.411267,
                type = "location"
            )
        ),
        // Skipped invalid entry: de:11000:900037168
        Station(
            id = "de:11000:900070405::2",
            name = "Ankogelweg",
            type = "station",
            weight = 751.75,
            location = Location(
                latitude = 52.419354,
                longitude = 13.398652,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900141506",
            name = "Am Steinberg",
            type = "station",
            weight = 2047.0,
            location = Location(
                latitude = 52.558383,
                longitude = 13.433301,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900027205::2",
            name = "Aalemannufer",
            type = "station",
            weight = 716.75,
            location = Location(
                latitude = 52.572957,
                longitude = 13.21284,
                type = "location"
            )
        ),
        Station(
            id = "de:11000:900182503::2",
            name = "Ahornallee",
            type = "station",
            weight = 675.25,
            location = Location(
                latitude = 52.455666,
                longitude = 13.619078,
                type = "location"
            )
        )
    )

}

