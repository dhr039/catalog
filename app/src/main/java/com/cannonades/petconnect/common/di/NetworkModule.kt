package com.cannonades.petconnect.common.di

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Also see: 'Stop passing Context into ViewModels'
 * https://iamgideon.medium.com/stop-passing-context-into-viewmodels-bb11b3f432fb
 *
 * Using the ApplicationContext in a ViewModel is not generally recommended because the ViewModel
 * outlives specific activity or fragment instances. However, using the ApplicationContext
 * (instead of a Context tied to a specific Activity or View) is usually safe because it is tied to
 * the lifecycle of the application itself, and does not hold any view-related context which could
 * lead to memory leaks.
 *
 * if you have:
 * @HiltViewModel
 * class NetworkViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
 * ...
 *     private val connectivityManager by lazy {
 *          context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
 *     }
 * ...
 * }
 * you're getting a warning from Android Studio, it may be due to a lint check that tries to
 * prevent any usage of context inside a ViewModel. This is generally a good check to have, since a
 * ViewModel should not be responsible for UI or view-related tasks.To solve this, instead of
 * injecting the ApplicationContext into your ViewModel, you could inject the ConnectivityManager
 * directly.
 *
 * So, instead, added this Module and use the ConnectivityService like this:
 * @HiltViewModel
 * class NetworkViewModel @Inject constructor(private val connectivityManager: ConnectivityManager) : ViewModel() {...}
 *
 * */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}