package com.iamsmk.shopify.Utilities.model

import com.google.gson.annotations.SerializedName

data class Products(
    @SerializedName("imgUrl") var imgUrl: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("slashPrice") var slashPrice: Int? = null
)
