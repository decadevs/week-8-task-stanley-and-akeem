package com.olamachia.pokemonweekseventask.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.Model.Move
import com.olamachia.maptrackerweekeighttask.R

class PokemonMovesAdapter(private var moveList: List<Move>) : RecyclerView.Adapter<PokemonMovesAdapter.ViewHolder>() {

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
       var singleDetail: TextView = itemView.findViewById(R.id.single_detail_tv)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.single_detail_item,parent,false)
        return ViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return moveList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.singleDetail.text=moveList[position].move?.name.toString()
    }


}
