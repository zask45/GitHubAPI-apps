package com.example.githubapi.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.R
import com.example.githubapi.data.remote.response.User
import com.example.githubapi.databinding.ActivityMainBinding
import com.example.githubapi.ui.adapter.UserAdapter
import com.example.githubapi.ui.favorite.FavoriteUserActivity
import com.example.githubapi.ui.settings.SettingPreferences
import com.example.githubapi.ui.settings.SettingsActivity
import com.example.githubapi.ui.settings.SettingsViewModel
import com.example.githubapi.ui.settings.SettingsViewModelFactory
import com.example.githubapi.ui.settings.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        showListItem()

        implementSettingTheme()

        activityMainBinding.searchBar.inflateMenu(R.menu.menu_options)

        activityMainBinding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_favorite -> {
                    val intent = Intent(this, FavoriteUserActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_setting -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


        with(activityMainBinding) {

            searchView.setupWithSearchBar(searchBar)

            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    mainViewModel.getUserList(searchView.text.toString())
                    mainViewModel.userList.observe(this@MainActivity) { list ->
                        if (list.isNullOrEmpty()) {
                            activityMainBinding.recyclerView.visibility = View.GONE
                            activityMainBinding.tvMessage.visibility = View.VISIBLE
                        } else {
                            activityMainBinding.tvMessage.visibility = View.GONE
                            activityMainBinding.recyclerView.visibility = View.VISIBLE
                            showListItem()
                        }
                    }
                    false
                }

        }
    }

    private fun implementSettingTheme() {
        val preferences = SettingPreferences.getInstance(application.dataStore)
        val settingsViewModel = ViewModelProvider(this, SettingsViewModelFactory(preferences))[SettingsViewModel::class.java]

        settingsViewModel.getTheme().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun showListItem() {
        mainViewModel.userList.observe(this) { userList ->
            showUserList(userList)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoadingIcon(isLoading)
        }

    }

    private fun showLoadingIcon(isLoading: Boolean) {
        activityMainBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUserList(userList: List<User>?) {
        val adapter = UserAdapter()
        adapter.submitList(userList)
        activityMainBinding.recyclerView.adapter = adapter
        activityMainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}