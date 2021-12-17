package com.olamachia.pokemonweekseventask.ui

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olamachia.maptrackerweekeighttask.R
import com.olamachia.pokemonweekseventask.Repository.PokemonRepository
import com.olamachia.pokemonweekseventask.adapter.PokemonListAdapter
import com.olamachia.pokemonweekseventask.api.GetPokeApi
import com.olamachia.pokemonweekseventask.api.RetrofitInstance
import com.olamachia.pokemonweekseventask.model.PokemonList
import com.olamachia.pokemonweekseventask.Utils.Constants.Companion.isOnline
import com.olamachia.pokemonweekseventask.Utils.ItemOffsetDecoration
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit


class PokemonListFragment(private var offset: Int, private var limit: Int) : Fragment() {
    lateinit var getPokeApi: GetPokeApi
    lateinit var recyclerView: RecyclerView
    private var retrofit: Retrofit = RetrofitInstance.instance
    private var pokemonList: PokemonList? = null
    private val compositeDisposable = CompositeDisposable()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pokemon_list, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPokeApi = retrofit.create(GetPokeApi::class.java)

        //Check if the phone has a connection
        if (isOnline(requireContext())) {
            //set recycler view dimensions and style
            recyclerView = requireView().findViewById(R.id.pokemon_recycler)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(activity, 2)
            val itemDecoration = ItemOffsetDecoration(requireActivity(), R.dimen.spacing)
            recyclerView.addItemDecoration(itemDecoration)
            // fetch pokemon
            getData(limit, offset)
        } else {
            Toast.makeText(
                requireContext(),
                "Network connection required",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getData(limit: Int, offset: Int) {
        //check if the list repository if it is empty fetch pokemon into it
        if (PokemonRepository.pokemonList == null) {
            //in the composite disposable make the Api call
            compositeDisposable.add(getPokeApi.getPokemonList(limit, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { error(Log.i("AAA", "Check Network Connection")) }
                .subscribe { pokeDex ->
                    pokemonList = pokeDex
                    //send the fetched data to the list in the repository
                    PokemonRepository.pokemonList = pokemonList?.results
                    PokemonRepository.pokemonList?.forEach {
                        // log the name and url for debugging
                        Log.i(TAG, "Pokemon: " + it.name)
                        Log.i(TAG, "Pokemon: " + it.url)
                    }
                    //send the list to the adapter and attach it to the recycler view
                    val adapter = PokemonListAdapter(PokemonRepository.pokemonList!!, limit, offset)
                    recyclerView.adapter = adapter
                }
            )
        } else {
            // if pokemon list in the repository is not empty, send it to the adapter
            val adapter = PokemonListAdapter(PokemonRepository.pokemonList!!, limit, offset)
            recyclerView.adapter = adapter
        }


    }


}