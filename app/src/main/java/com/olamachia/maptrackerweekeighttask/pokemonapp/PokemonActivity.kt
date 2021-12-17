package com.olamachia.maptrackerweekeighttask.pokemonapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.olamachia.maptrackerweekeighttask.MapsActivity
import com.olamachia.maptrackerweekeighttask.R
import com.olamachia.pokemonweekseventask.Utils.Constants
import com.olamachia.pokemonweekseventask.ui.PokemonListFragment

class PokemonActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)

        val limitText = findViewById<EditText>(R.id.limit_text)

        // What happens on the red go button click
        findViewById<Button>(R.id.go_btn).setOnClickListener {

            // check if the user left the number space empty
            if (limitText.text.isNullOrBlank()) {
                Toast.makeText(this, "Please select how many Pokemon characters you want to display.", Toast.LENGTH_LONG).show()
            } else {
                //convert the input to an integer
                val limit = limitText.text.toString().toInt()

                // Check if the user is connected
                if (Constants.isOnline(this)) {
                    //if they are connected, Toggle the visibility of the layout carrying the logo, pokemon number and button
                    findViewById<ConstraintLayout>(R.id.values_request).visibility = View.GONE
                    //Inflate the pokemon list fragment amd pass the entered limit to it
                    setFragment(PokemonListFragment(0, limit))
                } else {
                    //if they are not connected toast
                    Toast.makeText(this, "Network connection required", Toast.LENGTH_LONG).show()
                }
            }

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

    }

    private fun map() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }


    // function to set Fragments dynamically
    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_holder, fragment)
            commit()
        }
    }
}