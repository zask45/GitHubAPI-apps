package com.example.githubapi.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: SettingPreferences) : ViewModel() {
    fun getTheme(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun setTheme(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preferences.setThemeSetting(isDarkModeActive)
        }
    }
}