package com.example.githubapi.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.data.response.User
import com.example.githubapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        showListItem()

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

    private fun showListItem() {
        mainViewModel.userList.observe(this) {userList ->
            showUserList(userList)
        }

        mainViewModel.isLoading.observe(this) {isLoading ->
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