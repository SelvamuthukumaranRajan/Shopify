package com.iamsmk.shopify.Utilities.model

import com.google.gson.annotations.SerializedName

data class ProductsModel(
    @SerializedName("Products") var Products: ArrayList<Products> = arrayListOf()
)