package com.olamachia.pokemonweekseventask.api

import com.olamachia.pokemonweekseventask.Utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private var retInstance: Retrofit? = null

    val instance: Retrofit
        get() {
            if (retInstance == null)
                retInstance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retInstance!!
        }

}