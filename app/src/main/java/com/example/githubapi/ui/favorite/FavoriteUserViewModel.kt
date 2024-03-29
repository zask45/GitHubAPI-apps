package com.example.githubapi.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubapi.data.local.entity.FavoriteUserEntity
import com.example.githubapi.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavoriteUser(): LiveData<List<FavoriteUserEntity>>? = mFavoriteUserRepository.getAllFavoriteUser()
}