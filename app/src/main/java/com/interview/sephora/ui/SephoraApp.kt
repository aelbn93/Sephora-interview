package com.interview.sephora.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.interview.sephora.core.designsystem.component.SephoraBackground
import com.interview.sephora.core.navigation.Navigator
import com.interview.sephora.core.navigation.toEntries
import com.interview.sephora.feature.product.impl.navigation.productEntry

@Composable
internal fun SephoraApp(
    appState: SephoraAppState,
    modifier: Modifier = Modifier,
) {
    val navigator = remember { Navigator(appState.navigationState) }
    SephoraBackground(modifier = modifier) {
        Scaffold(
            modifier = modifier.semantics {
                testTagsAsResourceId = true
            },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {

                Box(
                    modifier = Modifier.consumeWindowInsets(
                        WindowInsets(0, 0, 0, 0),
                    ),
                ) {
                    val entryProvider = entryProvider {
                        productEntry(navigator)
                    }
                    NavDisplay(
                        entries = appState.navigationState.toEntries(entryProvider),
                        onBack = { navigator.goBack() },
                    )
                }
            }
        }
    }
}