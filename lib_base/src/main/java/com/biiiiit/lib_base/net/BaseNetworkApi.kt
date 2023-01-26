package com.biiiiit.lib_base.net

import com.biiiiit.lib_base.BaseApp
import com.biiiiit.lib_base.BuildConfig
import com.biiiiit.lib_base.utils.ONE_MB
import com.biiiiit.lib_base.utils.getOkHttpFile
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Author yo_hack
 * @Date 2021.10.13
 * @Description base network api
 **/
abstract class BaseNetworkApi(baseUrl: String) {


    private val retrofit: Retrofit by lazy {
        return@lazy enhanceRetrofitBuilder(
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        ).build()
    }


    abstract fun enhanceHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder

    abstract fun enhanceRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder

    /**
     * config http
     */
    private val okHttpClient: OkHttpClient
        get() {
            var builder = OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_MILLIS, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_MILLIS, TimeUnit.MILLISECONDS)
                .cache(Cache(getOkHttpFile(), ONE_MB * 10))
                .retryOnConnectionFailure(true)

            builder = enhanceHttpClientBuilder(builder)

            // dev only
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(ChuckerInterceptor(BaseApp.app))
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }
//      else {
//        builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
//      }

            return builder.build()
        }


    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

}



