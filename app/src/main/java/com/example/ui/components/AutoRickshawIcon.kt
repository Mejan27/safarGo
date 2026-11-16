package com.example.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.R

/**
 * SafarGo official Company Logo representing a Rajshahi Battery Auto-Rickshaw (টমটম).
 * Replaces previous bicycle/pedal-cycle icon with the official auto rickshaw symbol.
 */
@Composable
fun AutoRickshawIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = "SafarGo Auto Rickshaw Logo",
    tint: Color = LocalContentColor.current
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_auto_rickshaw),
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint
    )
}
