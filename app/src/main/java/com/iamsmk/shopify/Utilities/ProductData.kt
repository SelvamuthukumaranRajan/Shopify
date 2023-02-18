package com.iamsmk.shopify.Utilities

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.iamsmk.shopify.Utilities.model.ProductsModel

class ProductData(jsonString: String?) {
    private val gson: Gson = GsonBuilder().create()
    val productList: ProductsModel =
        gson.fromJson<ProductsModel>(jsonString, object :
            TypeToken<ProductsModel>() {}.type)
}