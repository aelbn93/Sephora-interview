package com.interview.sephora.feature.product.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.sephora.core.data.repository.ProductRepository
import com.interview.sephora.core.domain.GetProductsUseCase
import com.interview.sephora.core.model.Product
import com.interview.sephora.core.model.ReviewSortField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {


    private val _sortField = MutableStateFlow(ReviewSortField.BEST_TO_WORST)
    val sortField: StateFlow<ReviewSortField> = _sortField.asStateFlow()
    private val _syncError = MutableStateFlow<ProductUiState>(ProductUiState.Loading)

    val searchQuery = savedStateHandle.getStateFlow(
        key = SEARCH_QUERY,
        initialValue = "",
    )
    val uiState: StateFlow<ProductUiState> = combine(
        searchQuery.debounce(200L),
        sortField,
        _syncError
    ) { query, sort, syncState -> Triple(query, sort, syncState) }
        .flatMapLatest { (query, sort, syncState) ->
            if (syncState is ProductUiState.Error) {
                return@flatMapLatest kotlinx.coroutines.flow.flowOf(ProductUiState.Error)
            }
            getProductsUseCase(query = query, sortBy = sort)
                .map<List<Product>, ProductUiState> { ProductUiState.Success(it) }
                .catch { emit(ProductUiState.Error) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProductUiState.Loading,
        )

    init {
        sync()
    }

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun onSortSelected(sortField: ReviewSortField) {
        _sortField.update { sortField }
    }

    private fun sync() = viewModelScope.launch {
        runCatching { productRepository.sync() }.onFailure { _syncError.update { ProductUiState.Error } }
    }
}


private const val SEARCH_QUERY = "searchQuery"