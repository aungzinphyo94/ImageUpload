package com.azp.photoupload.api

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {

//    private val apiInterface: ApiInterface
    private val retrofit: Retrofit

    companion object {
        val BASE_URL = "http://online-shopping.khaingthinkyi.me/api/setup/"
    }

    init {
        val okHttpClient = OkHttpClient.Builder()
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    fun apiInterface(): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

}