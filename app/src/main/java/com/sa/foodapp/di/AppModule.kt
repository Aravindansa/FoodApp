package com.sa.foodapp.di

import android.app.Application
import com.sa.foodapp.data.data_source.remote.Api
import com.sa.foodapp.data.data_source.remote.ResultCallAdapterFactory
import com.sa.foodapp.data.repository.FoodRepository
import com.sa.foodapp.domain.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApi (): Api {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client= OkHttpClient.Builder().apply {
            addInterceptor(logging)
        }.build()
        val retrofit= Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
        return retrofit.create(Api::class.java)
    }

    @Singleton
    @Provides
    fun provideDataRepository(api: Api, app: Application):FoodRepository{
        return FoodRepository(api,app)
    }

}