package com.cannonades.petconnect.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.cannonades.petconnect.animalsnearyou.presentation.AnimalsNearYouEvent
import com.cannonades.petconnect.animalsnearyou.presentation.AnimalsNearYouFragmentViewModel
import com.cannonades.petconnect.common.presentation.ui.AppContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AnimalsNearYouFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onEvent(AnimalsNearYouEvent.RequestInitialAnimalsList)

        setContent {
            AppContent(viewModel)
        }
    }
}