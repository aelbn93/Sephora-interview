package com.interview.sephora.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProduct(
    @SerialName("product_id")
    val productId: Long? = null,
    @SerialName("product_name")
    val productName: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("price")
    val price: Double? = null,
    @SerialName("images_url")
    val imagesUrl: NetworkImagesUrl? = null,
    @SerialName("c_brand")
    val brand: NetworkBrand? = null,
    @SerialName("is_productSet")
    val isProductSet: Boolean? = null,
    @SerialName("is_special_brand")
    val isSpecialBrand: Boolean? = null,
)

@Serializable
data class NetworkImagesUrl(
    @SerialName("small")
    val small: String? = null,
    @SerialName("large")
    val large: String? = null,
)

@Serializable
data class NetworkBrand(
    @SerialName("id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null,
)