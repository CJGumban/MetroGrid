package com.example.metrogrid

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


class NetworkObserver @Inject constructor(
    @ApplicationContext private val context: Context) {
    fun observe(): Flow<Boolean> = callbackFlow {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val scope = CoroutineScope(Dispatchers.IO)

        fun checkInternet(): Boolean{
            return try {
                val connection = URL("https://clients3.google.com/generate_204")
                    .openConnection() as HttpURLConnection
                connection.connectTimeout = 1500
                connection.readTimeout = 1500
                connection.instanceFollowRedirects = false
                connection.connect()

                connection.responseCode == 204
            } catch (e: Exception){
                false
            }

        }
        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                scope.launch {
                    trySend(checkInternet())
                }
            }

            override fun onLost(network: Network) {
                trySend(false)
            }

            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
                scope.launch {
                    trySend(checkInternet())
                }
            }
        }

        cm.registerDefaultNetworkCallback(callback)

        // Send initial state
        val initial = cm.activeNetwork?.let {
            checkInternet()
        } ?: false
        trySend(initial)

        awaitClose {
            cm.unregisterNetworkCallback(callback)
            scope.cancel()
        }
    }
}