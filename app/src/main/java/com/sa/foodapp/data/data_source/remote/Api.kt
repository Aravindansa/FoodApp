package com.sa.foodapp.data.data_source.remote

import com.sa.foodapp.domain.model.FoodListRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("search.php")
    suspend fun searchMeal(@Query("s") query: String): Response<FoodListRes>
}