package com.interview.sephora.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProductReview(
    @SerialName("product_id")
    val productId: Long? = null,
    @SerialName("hide")
    val hide: Boolean? = null,
    @SerialName("reviews")
    val reviews: List<NetworkReview>? = null,
)

@Serializable
data class NetworkReview(
    @SerialName("name")
    val name: String? = null,
    @SerialName("text")
    val text: String? = null,
    @SerialName("rating")
    val rating: Double? = null
)