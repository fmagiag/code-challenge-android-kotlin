package com.arctouch.codechallenge

import android.app.Application
import com.arctouch.codechallenge.api.TmdbApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class AppApplication : Application() {
    companion object {
        val logging = HttpLoggingInterceptor()
        val api: TmdbApi

        init {
            logging.level = HttpLoggingInterceptor.Level.BODY
            api = Retrofit.Builder()
                    .baseUrl(TmdbApi.URL)
                    .client(OkHttpClient.Builder().addInterceptor(logging).build())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(TmdbApi::class.java)
        }
    }
}
