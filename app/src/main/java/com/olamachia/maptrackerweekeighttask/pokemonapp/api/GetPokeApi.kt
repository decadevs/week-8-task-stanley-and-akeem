package com.olamachia.pokemonweekseventask.api


import com.olamachia.pokemonweekseventask.model.PokemonDetails
import com.olamachia.pokemonweekseventask.model.PokemonList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetPokeApi {

    @GET("pokemon")
    fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int): Observable<PokemonList>

    @GET("pokemon/{name}")
    fun getPokemonDetails(
        @Path("name") name:String):Observable<PokemonDetails>


}