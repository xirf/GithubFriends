package com.andika.githubfriend.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.andika.githubfriend.R
import com.andika.githubfriend.databinding.ActivityMainBinding
import com.andika.githubfriend.preferences.SettingPreferences
import com.andika.githubfriend.preferences.dataStore
import com.andika.githubfriend.viewmodels.MainViewModel
import com.andika.githubfriend.viewmodels.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(pref, application)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]


        viewModel.getThemeSettings().observe(this) {
            AppCompatDelegate.setDefaultNightMode(
                if (it) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = binding.navViewBottomNavigation

        navView.setupWithNavController(navController)
    }

}

