package com.olamachia.pokemonweekseventask.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.olamachia.maptrackerweekeighttask.R
import com.olamachia.pokemonweekseventask.model.Stat

class PokemonStatAdapter(private var statList: List<Stat>) : RecyclerView.Adapter<PokemonStatAdapter.ViewHolder>() {
    lateinit var statItem:String

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
       var singleDetail: TextView = itemView.findViewById(R.id.single_detail_tv)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.single_detail_item,parent,false)
        return ViewHolder(itemView)
    }



    override fun getItemCount(): Int {
        return statList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        statItem = statList[position].stat?.name +": "+ statList[position].base_stat
        holder.singleDetail.text=statItem
    }


}
