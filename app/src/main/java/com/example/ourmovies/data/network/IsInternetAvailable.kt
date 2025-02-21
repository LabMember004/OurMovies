package com.example.ourmovies.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.content.Context
import android.widget.Toast

class IsInternetAvailable {
    companion object {
        fun checkInternetAvailability(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                return activeNetworkInfo?.isConnected == true
            }
        }
    }
}
