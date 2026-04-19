package com.interview.sephora.feature.product.impl

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.interview.sephora.core.model.ReviewSortField
import com.interview.sephora.core.testing.fake.sampleProducts
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var loadingContentDesc: String
    private lateinit var errorMessage: String
    private lateinit var searchPlaceholder: String
    private lateinit var clearSearchContentDesc: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            loadingContentDesc = getString(R.string.feature_product_impl_loading_products)
            errorMessage = getString(R.string.feature_product_impl_something_happened)
            searchPlaceholder = getString(R.string.feature_product_impl_search_products)
            clearSearchContentDesc = getString(R.string.feature_product_impl_clear_search)
        }
    }

    @Test
    fun loadingWheel_whenUiStateIsLoading_isDisplayed() {
        composeTestRule.setContent {
            ProductsScreen(
                uiState = ProductUiState.Loading,
                searchQuery = "",
                onSearchQueryChanged = {},
                currentSortField = ReviewSortField.BEST_TO_WORST,
                onSortSelected = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(loadingContentDesc)
            .assertIsDisplayed()
    }

    @Test
    fun errorMessage_whenUiStateIsError_isDisplayed() {
        composeTestRule.setContent {
            ProductsScreen(
                uiState = ProductUiState.Error,
                searchQuery = "",
                onSearchQueryChanged = {},
                currentSortField = ReviewSortField.BEST_TO_WORST,
                onSortSelected = {},
            )
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun products_whenUiStateIsSuccess_areDisplayed() {
        composeTestRule.setContent {
            ProductsScreen(
                uiState = ProductUiState.Success(sampleProducts),
                searchQuery = "",
                onSearchQueryChanged = {},
                currentSortField = ReviewSortField.BEST_TO_WORST,
                onSortSelected = {},
            )
        }

        sampleProducts.forEach { product ->
            composeTestRule
                .onNodeWithText(product.productName)
                .assertIsDisplayed()
        }
    }

    @Test
    fun searchBar_whenUiStateIsSuccess_isDisplayed() {
        composeTestRule.setContent {
            ProductsScreen(
                uiState = ProductUiState.Success(sampleProducts),
                searchQuery = "",
                onSearchQueryChanged = {},
                currentSortField = ReviewSortField.BEST_TO_WORST,
                onSortSelected = {},
            )
        }

        composeTestRule
            .onNodeWithText(searchPlaceholder)
            .assertIsDisplayed()
    }

    @Test
    fun clearButton_whenSearchQueryIsNotEmpty_isDisplayed() {
        composeTestRule.setContent {
            ProductsScreen(
                uiState = ProductUiState.Success(sampleProducts),
                searchQuery = "Serum",
                onSearchQueryChanged = {},
                currentSortField = ReviewSortField.BEST_TO_WORST,
                onSortSelected = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(clearSearchContentDesc)
            .assertIsDisplayed()
    }

    @Test
    fun clearButton_whenSearchQueryIsEmpty_isNotDisplayed() {
        composeTestRule.setContent {
            ProductsScreen(
                uiState = ProductUiState.Success(sampleProducts),
                searchQuery = "",
                onSearchQueryChanged = {},
                currentSortField = ReviewSortField.BEST_TO_WORST,
                onSortSelected = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(clearSearchContentDesc)
            .assertDoesNotExist()
    }

    @Test
    fun clearButton_whenClicked_callsOnSearchQueryChangedWithEmptyString() {
        var capturedQuery: String? = null

        composeTestRule.setContent {
            ProductsScreen(
                uiState = ProductUiState.Success(sampleProducts),
                searchQuery = "Serum",
                onSearchQueryChanged = { capturedQuery = it },
                currentSortField = ReviewSortField.BEST_TO_WORST,
                onSortSelected = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription(clearSearchContentDesc)
            .performClick()

        assert(capturedQuery == "")
    }

    @Test
    fun searchBar_whenTextEntered_callsOnSearchQueryChanged() {
        var capturedQuery: String? = null

        composeTestRule.setContent {
            ProductsScreen(
                uiState = ProductUiState.Success(sampleProducts),
                searchQuery = "",
                onSearchQueryChanged = { capturedQuery = it },
                currentSortField = ReviewSortField.BEST_TO_WORST,
                onSortSelected = {},
            )
        }

        composeTestRule
            .onNodeWithText(searchPlaceholder)
            .performTextInput("Serum")

        assert(capturedQuery == "Serum")
    }
}