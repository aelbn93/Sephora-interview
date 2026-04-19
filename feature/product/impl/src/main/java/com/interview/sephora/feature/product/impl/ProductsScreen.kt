package com.interview.sephora.feature.product.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.interview.sephora.core.designsystem.component.SephoraLoadingWheel
import com.interview.sephora.core.model.ReviewSortField
import com.interview.sephora.core.ui.SortFab

@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentSortField by viewModel.sortField.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    ProductsScreen(
        uiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        currentSortField = currentSortField,
        onSortSelected = viewModel::onSortSelected,
        modifier = modifier,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductsScreen(
    uiState: ProductUiState,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    currentSortField: ReviewSortField,
    onSortSelected: (ReviewSortField) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.feature_product_impl_products)) },
            )
        },
        floatingActionButton = {
            SortFab(
                currentSortField = currentSortField,
                onSortSelected = onSortSelected,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (uiState) {
                ProductUiState.Loading -> SephoraLoadingWheel(
                    modifier = modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    contentDesc = stringResource(R.string.feature_product_impl_loading_products)
                )

                ProductUiState.Error -> Text(
                    modifier = modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    text = stringResource(R.string.feature_product_impl_something_happened)
                )

                is ProductUiState.Success -> {
                    ProductsContent(
                        products = uiState.products,
                        searchQuery = searchQuery,
                        onSearchQueryChanged = onSearchQueryChanged,
                    )
                }
            }
        }
    }
}

