package com.sa.foodapp.data.repository

import android.app.Application
import android.util.Log
import com.sa.foodapp.data.data_source.remote.Api
import com.sa.foodapp.data.data_source.remote.Resource
import com.sa.foodapp.domain.model.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class FoodRepository(private val api:Api,
                     private val application:Application) {

    fun searchMeal(query: String): Flow<Resource<List<Meal?>?>>{
        return flow<Resource<List<Meal?>?>> {
            emit(Resource.Loading())
            try {
                val repose=api.searchMeal(query)
                if (repose.isSuccessful){
                    if (repose.body()!=null){
                        emit(Resource.Success(repose.body()?.meals))
                    }else{
                        emit(Resource.Error(repose.message()))
                    }
                }else{
                    emit(Resource.Error(repose.message()))
                }
            }catch (e:Exception){
                e.printStackTrace()
                val errorMessage = when (e) {
                    is IOException -> "please check your internet connection"
                    else -> e.localizedMessage
                }
                emit(Resource.Error(errorMessage))
            }

        }.flowOn(Dispatchers.IO)
    }

}