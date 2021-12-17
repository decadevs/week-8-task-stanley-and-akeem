package com.olamachia.pokemonweekseventask.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.olamachia.maptrackerweekeighttask.R
import com.olamachia.pokemonweekseventask.Utils.Constants
import com.olamachia.pokemonweekseventask.Utils.ItemOffsetDecoration
import com.olamachia.pokemonweekseventask.adapter.PokemonDetailsAdapter
import com.olamachia.pokemonweekseventask.adapter.PokemonMovesAdapter
import com.olamachia.pokemonweekseventask.adapter.PokemonStatAdapter
import com.olamachia.pokemonweekseventask.api.GetPokeApi
import com.olamachia.pokemonweekseventask.api.RetrofitInstance
import com.olamachia.pokemonweekseventask.model.ImageUploadResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import io.reactivex.observers.TestObserver
import retrofit2.Call


class PokemonDetailsFragment(name:String) : Fragment() {

    private lateinit var getPokeApi: GetPokeApi
    private lateinit var recyclerViewDetails: RecyclerView
    private lateinit var recyclerViewMoves: RecyclerView
    private lateinit var recyclerViewStats: RecyclerView
    private var retrofit: Retrofit = RetrofitInstance.instance
    private lateinit var pokemonName: TextView
    private val compositeDisposable= CompositeDisposable()
    private val receivedName = name
    lateinit var pokemonImage:ImageView
    private lateinit var pokemonHeight:TextView
    private lateinit var pokemonWeight:TextView
    private lateinit var pokemonId:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pokemon_details, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPokeApi = retrofit.create(GetPokeApi::class.java)

        //Save views to variables
        pokemonImage= view.findViewById(R.id.pokemon_details_image)
        pokemonName = view.findViewById(R.id.pokemon_details_name)
        pokemonHeight = view.findViewById(R.id.height_number)
        pokemonId= view.findViewById(R.id.id_tv)
        pokemonWeight =view.findViewById(R.id.weight_number)
        recyclerViewDetails = requireView().findViewById(R.id.pokemon_abilities_recycler)
        recyclerViewMoves = requireView().findViewById(R.id.pokemon_moves_recycler)
        recyclerViewStats = requireView().findViewById(R.id.pokemon_stats_recycler)

         //check connectivity and make Api call
        if(Constants.isOnline(requireContext())){
            getDetails(receivedName)
        }else{
            Toast.makeText(requireContext(),"Network connection required", Toast.LENGTH_LONG).show()
        }
    }

    // the function that makes the calls
    private fun getDetails(receivedName: String) {
        val itemDecoration = ItemOffsetDecoration(requireActivity(), R.dimen.spacing)

            compositeDisposable.add(getPokeApi.getPokemonDetails(receivedName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { error(Toast.makeText(requireContext(),"Check network connection required", Toast.LENGTH_LONG).show()) }
                .subscribe { pokemonDetails ->
                    val retrievedAbilityList = pokemonDetails?.abilities!!
                    val retrievedMovesList = pokemonDetails.moves!!
                    val retrievedStatsList = pokemonDetails.stats!!
                    val pokeWeightValue =pokemonDetails.weight.toString()+"kg"
                    val identity =pokemonDetails.id

                    pokemonName.text = pokemonDetails.name.toString()
                    pokemonHeight.text= pokemonDetails.height.toString()
                    pokemonWeight.text= pokeWeightValue
                    pokemonId.text=identity.toString()

                    Glide.with(requireContext())
                        .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${identity}.png")
                        .into(pokemonImage)


                    retrievedAbilityList.forEach{
                        Log.i("AAAA", "PokemonAbilities: " + it.ability?.name)
                    }

                    retrievedMovesList.forEach{
                        Log.i("BBBB", "PokemonMoves: " + it.move?.name)
                    }

                    retrievedStatsList.forEach {
                        Log.i("CCCC", "PokeStats: " + it.base_stat)
                    }

                    val abilityAdapter = PokemonDetailsAdapter(retrievedAbilityList)
                    recyclerViewDetails.setHasFixedSize(true)
                    recyclerViewDetails.layoutManager= LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                    recyclerViewDetails.addItemDecoration(itemDecoration)
                    recyclerViewDetails.adapter = abilityAdapter

                    val movesAdapter = PokemonMovesAdapter(retrievedMovesList)
                    recyclerViewMoves.setHasFixedSize(true)
                    recyclerViewMoves.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                    recyclerViewMoves.addItemDecoration(itemDecoration)
                    recyclerViewMoves.adapter = movesAdapter

                    val statsAdapter = PokemonStatAdapter(retrievedStatsList)
                    recyclerViewStats.setHasFixedSize(true)
                    recyclerViewStats.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
                    recyclerViewStats.addItemDecoration(itemDecoration)
                    recyclerViewStats.adapter = statsAdapter


                }


            )


    }

}