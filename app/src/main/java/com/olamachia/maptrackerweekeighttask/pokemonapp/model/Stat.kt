package com.olamachia.pokemonweekseventask.model
import com.decagon.android.sq007.Model.StatType

data class Stat (
    var stat: StatType? = null,
    var base_stat:Int
)