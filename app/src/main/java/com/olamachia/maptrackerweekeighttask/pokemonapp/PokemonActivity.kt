package com.olamachia.maptrackerweekeighttask.pokemonapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.olamachia.maptrackerweekeighttask.MapsActivity
import com.olamachia.maptrackerweekeighttask.R

class PokemonActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)


        //initialize the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        //set image upload activity selected
        bottomNavigationView.selectedItemId = R.id.pokemon

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.pokemon -> null
                R.id.map -> map()

            }
            true
        }
    }
    private fun map() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}