package com.olamachia.pokemonweekseventask.Repository

import com.olamachia.pokemonweekseventask.model.Pokemon
import com.olamachia.pokemonweekseventask.model.PokemonDetails

class PokemonRepository {

    companion object {
        var pokemonList: List<Pokemon>? = null
        var singlePokemon: List<PokemonDetails>? = null
    }
}