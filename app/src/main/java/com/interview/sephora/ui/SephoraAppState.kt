package com.interview.sephora.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.interview.sephora.core.navigation.NavigationState
import com.interview.sephora.core.navigation.rememberNavigationState
import com.interview.sephora.feature.product.api.ProductNavKey


@Composable
fun rememberSephoraAppState(): SephoraAppState {
    val navigationState = rememberNavigationState(ProductNavKey, setOf(ProductNavKey))
    return remember(navigationState) {
        SephoraAppState(navigationState)
    }
}

@Stable
class SephoraAppState(
    val navigationState: NavigationState
)