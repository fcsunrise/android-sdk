package com.atlas.sdk.data.network.api

import com.atlas.sdk.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal object RetrofitBuilder {

    private const val CONNECT_TIMEOUT = 60L
    private const val READ_TIMEOUT = 60L
    private const val BASE_URL = "https://json-m.fcsunrise.com/"

    private fun provideHttpClient(): OkHttpClient {
        val httpInterceptor = HttpLoggingInterceptor()
        httpInterceptor.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val okHttpClientBuilder =
                OkHttpClient
                        .Builder()
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                        .addInterceptor(httpInterceptor)
                        .addInterceptor {
                            it.run {
                                proceed(
                                        request()
                                                .newBuilder()
                                                .header("Content-Type", "application/json; charset=utf-8")
                                                .build()
                                )
                            }
                        }
        return okHttpClientBuilder.build()
    }

    private val retrofit by lazy{
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(provideHttpClient())
                .build()
    }

    internal val apiService: ApiAtlas by lazy {
        retrofit.create(ApiAtlas::class.java)
    }
}