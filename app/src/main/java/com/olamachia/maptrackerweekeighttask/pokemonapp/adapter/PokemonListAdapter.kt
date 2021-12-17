package com.olamachia.pokemonweekseventask.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.olamachia.maptrackerweekeighttask.R
import com.olamachia.pokemonweekseventask.model.Pokemon
import com.olamachia.pokemonweekseventask.ui.PokemonDetailsFragment

class PokemonListAdapter(
    private var pokemonList: List<Pokemon>, limit: Int, private val offset: Int): RecyclerView.Adapter<PokemonListAdapter.ViewHolder>(){

     var identity:Int = 0


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var pokeName = itemView.findViewById<TextView>(R.id.pokemon_name)
        private var pokeImage = itemView.findViewById<ImageView>(R.id.pokemon_image)
        private var pokeId =itemView.findViewById<TextView>(R.id.pokemon_id)
        private var pokeBackground=itemView.findViewById<ConstraintLayout>(R.id.pokemon_card_background)

        fun bind(pokemon: Pokemon)
        = with(itemView) {
          identity = adapterPosition+1
            pokeName.text = pokemonList[adapterPosition].name!!.uppercase()
            pokeId.text = identity.toString()


            Glide.with(context).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${identity}.png").into(pokeImage)
            val blue =(0..255).random()
            val red =(0..255).random()
            val currentColor= Color.rgb( red, 211, blue)
            pokeBackground.backgroundTintList = ColorStateList.valueOf(currentColor)

            pokeBackground.setOnClickListener {
                val appCompatActivity = it.context as AppCompatActivity
                appCompatActivity.supportFragmentManager.
                beginTransaction()
                    //pass the name to the next fragment
                    .replace(R.id.fragment_holder, PokemonDetailsFragment(pokemonList[adapterPosition].name!!))
                    .addToBackStack(null)
                    .commit()

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.pokemon_card_layout,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pokemonList[position])
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }


}