package com.cannonades.petconnect.common.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import com.cannonades.petconnect.R

@Composable
fun NoInternetWarning() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.no_internet_connection),
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic)
        )
    }
}