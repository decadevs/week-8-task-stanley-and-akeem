package com.olamachia.pokemonweekseventask.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSendInstance {
    private var retSendInstance:Retrofit?=null
    val instanceSend: Retrofit?

        get(){
            if (retSendInstance == null)
                retSendInstance = Retrofit.Builder()
                .baseUrl("https://darot-image-upload-service.herokuapp.com/api/v1/upload")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retSendInstance
        }
    }



