@file:Suppress("DEPRECATION")

package com.dehdarian.task

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dehdarian.task.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity of Project
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val window = this.window
        /**
         * Clear FLAG_TRANSLUCENT_STATUS from the window
         */
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        /**
         * Add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS to the window
         */
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        /**
         * Finally change the color
         */
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        /**
         * Passing each menu ID as a set of Ids because each
         * menu should be considered as top level destinations.
         */
        AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_news, R.id.navigation_search,R.id.navigation_profile
            )
        )
        navView.setupWithNavController(navController)
    }
}