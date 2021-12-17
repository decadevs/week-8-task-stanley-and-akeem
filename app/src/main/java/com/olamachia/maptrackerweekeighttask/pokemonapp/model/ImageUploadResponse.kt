package com.olamachia.pokemonweekseventask.model

data class ImageUploadResponse (
    val error:String,
    val message:String,
    val status : String,
    val  image:String?=null
        )