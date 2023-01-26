package com.biiiiit.lib_base.net

import android.os.Build
import com.biiiiit.lib_base.BuildConfig
import com.biiiiit.lib_base.user.getToken
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @Author yo_hack
 * @Date 2020.09.18
 * @Description
 **/
class KokoNetApi(baseUrl: String) : BaseNetworkApi(baseUrl) {


    companion object {
        val INSTANCE: KokoNetApi by lazy { KokoNetApi(BuildConfig.HOST) }
        private val UA =
            "board='${Build.BOARD}'  brand='${Build.BRAND}' device='${Build.DEVICE}' model='${Build.MODEL}' sdkInt='${Build.VERSION.SDK_INT}'"

        var token: String? = getToken()
    }


    override fun enhanceHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder.apply {

            builder.addInterceptor {
                val request = it.request()

                val t = request.header(AUTH_STR)

                val newBuilder = request.newBuilder()
                newBuilder.addHeader("User-Agent", UA)
                newBuilder.addHeader("appVersion", BuildConfig.V_CODE)
                newBuilder.addHeader("appPlatform", "android")

                if (t.isNullOrBlank() && !token.isNullOrBlank()) {
                    newBuilder.addHeader(AUTH_STR, "Bearer $token")
                }

                return@addInterceptor it.proceed(newBuilder.build())

            }


        }
    }

    override fun enhanceRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder
    }


}
