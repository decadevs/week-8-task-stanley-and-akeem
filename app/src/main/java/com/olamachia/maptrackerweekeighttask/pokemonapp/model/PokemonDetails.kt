package com.olamachia.pokemonweekseventask.model

import com.decagon.android.sq007.Model.Ability
import com.decagon.android.sq007.Model.Move


class PokemonDetails {
    var abilities: List<Ability>? = null
    var type: List<String>? = null
    var height: Double? = null
    var weight: Double? = null
    var base_experience:Double? = null
    var img: String? = null
    var name: String? = null
    var moves: List<Move>? = null
    var stats: List<Stat>? = null
    var id:Int? = null
}