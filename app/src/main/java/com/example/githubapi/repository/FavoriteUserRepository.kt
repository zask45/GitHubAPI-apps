package com.example.githubapi.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubapi.data.local.entity.FavoriteUserEntity
import com.example.githubapi.data.local.room.FavoriteUserDao
import com.example.githubapi.data.local.room.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val database = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = database.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUserEntity>>? = mFavoriteUserDao.getAllFavoriteUser()

    fun isFavorite(username: String): LiveData<Boolean> = mFavoriteUserDao.isFavoriteUser(username)

    fun insert(favoriteUser: FavoriteUserEntity) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUserEntity) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }

    fun setFavoriteUser(favoriteUser: FavoriteUserEntity, favoriteState: Boolean) {
        executorService.execute {
            favoriteUser.isFavorite = favoriteState
            mFavoriteUserDao.update(favoriteUser)
        }
    }

}