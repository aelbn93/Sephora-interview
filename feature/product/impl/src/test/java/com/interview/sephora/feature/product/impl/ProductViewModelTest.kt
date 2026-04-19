package com.interview.sephora.feature.product.impl

import androidx.lifecycle.SavedStateHandle
import com.interview.sephora.core.domain.GetProductsUseCase
import com.interview.sephora.core.model.ReviewSortField
import com.interview.sephora.core.testing.fake.sampleProducts
import com.interview.sephora.core.testing.repository.TestProductRepository
import com.interview.sephora.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ProductViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val productRepository = TestProductRepository()

    private lateinit var viewModel: ProductViewModel

    @Before
    fun setup() {
        viewModel = ProductViewModel(
            getProductsUseCase = GetProductsUseCase(productRepository),
            productRepository = productRepository,
            savedStateHandle = SavedStateHandle(),
        )
    }

    @Test
    fun `uiState is Loading on start`() = runTest {
        assertEquals(ProductUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `uiState is Success when products are loaded`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }

        productRepository.sendProducts(sampleProducts)

        testScheduler.advanceUntilIdle()

        assertIs<ProductUiState.Success>(viewModel.uiState.value)
        assertEquals(sampleProducts, (viewModel.uiState.value as ProductUiState.Success).products)

        collectJob.cancel()
    }

    @Test
    fun `uiState filters products when search query changes`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }

        productRepository.sendProducts(sampleProducts)

        viewModel.onSearchQueryChanged("Serum")

        // Allow debounce (200ms) to pass
        testScheduler.advanceTimeBy(300L)

        val state = viewModel.uiState.value
        assertIs<ProductUiState.Success>(state)
        assertEquals(1, state.products.size)
        assertEquals("Rose Serum", state.products.first().productName)

        collectJob.cancel()
    }

    @Test
    fun `uiState returns all products when search query is cleared`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }

        productRepository.sendProducts(sampleProducts)

        viewModel.onSearchQueryChanged("Serum")
        testScheduler.advanceTimeBy(300L)

        viewModel.onSearchQueryChanged("")
        testScheduler.advanceTimeBy(300L)

        val state = viewModel.uiState.value
        assertIs<ProductUiState.Success>(state)
        assertEquals(sampleProducts.size, state.products.size)

        collectJob.cancel()
    }

    @Test
    fun `sortField defaults to BEST_TO_WORST`() = runTest {
        assertEquals(ReviewSortField.BEST_TO_WORST, viewModel.sortField.value)
    }

    @Test
    fun `onSortSelected updates sortField`() = runTest {
        viewModel.onSortSelected(ReviewSortField.WORST_TO_BEST)
        assertEquals(ReviewSortField.WORST_TO_BEST, viewModel.sortField.value)
    }

    @Test
    fun `uiState reacts to sort change`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }

        productRepository.sendProducts(sampleProducts)
        testScheduler.advanceUntilIdle()

        viewModel.onSortSelected(ReviewSortField.WORST_TO_BEST)
        testScheduler.advanceUntilIdle()

        assertIs<ProductUiState.Success>(viewModel.uiState.value)

        collectJob.cancel()
    }
}