package com.sa.foodapp.domain.model


import com.google.gson.annotations.SerializedName

data class FoodListRes(
    @SerializedName("meals")
    val meals: List<Meal?>?
)