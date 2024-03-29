package com.example.githubapi.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.R
import com.example.githubapi.data.remote.response.User
import com.example.githubapi.databinding.ActivityFavoriteUserBinding
import com.example.githubapi.helper.ViewModelFactory
import com.example.githubapi.ui.adapter.UserAdapter
import com.example.githubapi.ui.settings.SettingsActivity


class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var activityFavoriteUserBinding: ActivityFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFavoriteUserBinding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(activityFavoriteUserBinding.root)

        val favoriteUserViewModel = obtainViewModel(this)

        setSupportActionBar(activityFavoriteUserBinding.appBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        onBackIconPressed()

        favoriteUserViewModel.getAllFavoriteUser()?.observe(this) { favoriteUserList ->
            if (favoriteUserList.isNullOrEmpty() ) {
                activityFavoriteUserBinding.recyclerView.visibility = View.GONE
                activityFavoriteUserBinding.tvMessage.visibility = View.VISIBLE
            } else {
                activityFavoriteUserBinding.tvMessage.visibility = View.GONE
                activityFavoriteUserBinding.recyclerView.visibility = View.VISIBLE

                val list = mutableListOf<User>()

                favoriteUserList.map {
                    val item = User(login = it.username, avatarUrl = it.avatarUrl, id = it.id)
                    list.add(item)
                }
                val adapter = UserAdapter()
                adapter.submitList(list)
                activityFavoriteUserBinding.recyclerView.adapter = adapter
                activityFavoriteUserBinding.recyclerView.layoutManager = LinearLayoutManager(this)
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        menu.findItem(R.id.menu_favorite).isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_setting) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onBackIconPressed() {
        activityFavoriteUserBinding.appBar.setNavigationOnClickListener {
            this.finish()
        }
    }
}