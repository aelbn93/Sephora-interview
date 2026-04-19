package com.interview.sephora.feature.product.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.interview.sephora.core.navigation.Navigator
import com.interview.sephora.feature.product.api.ProductNavKey
import com.interview.sephora.feature.product.impl.ProductsScreen

fun EntryProviderScope<NavKey>.productEntry(navigator: Navigator) {
    entry<ProductNavKey> {
        ProductsScreen()
    }
}
