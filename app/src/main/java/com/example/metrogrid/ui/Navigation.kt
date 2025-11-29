package com.example.metrogrid.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.metrogrid.ui.station.details.StationDetails
import com.example.metrogrid.ui.station.details.StationDetailsRoute
import com.example.metrogrid.ui.station.list.StationList
import com.example.metrogrid.ui.station.list.StationListRoute

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = StationList){
        composable<StationList>{
            StationListRoute(
                    navBackStackEntry = it,
                    navController = navController

            )
        }
        composable<StationDetails> {
            StationDetailsRoute(
                navBackStackEntry = it,
                navController = navController
            )
        }


    }

}