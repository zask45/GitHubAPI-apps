package com.example.githubapi.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubapi.ui.main.MainActivity
import com.example.githubapi.ui.settings.SettingPreferences
import com.example.githubapi.ui.settings.SettingsViewModel
import com.example.githubapi.ui.settings.SettingsViewModelFactory
import com.example.githubapi.ui.settings.dataStore

@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = SettingPreferences.getInstance(application.dataStore)

        val themeSettingViewModel = ViewModelProvider(this, SettingsViewModelFactory(preferences))[SettingsViewModel::class.java]
        themeSettingViewModel.getTheme().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            },
            2000
        )
    }
}