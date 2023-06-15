package com.cannonades.petconnect.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.cannonades.petconnect.animalsnearyou.presentation.AnimalsNearYouEvent
import com.cannonades.petconnect.animalsnearyou.presentation.AnimalsNearYouFragmentViewModel
import com.cannonades.petconnect.common.presentation.ui.theme.PetConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AnimalsNearYouFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onEvent(AnimalsNearYouEvent.RequestInitialAnimalsList)

        setContent {
            PetConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }
    }
}