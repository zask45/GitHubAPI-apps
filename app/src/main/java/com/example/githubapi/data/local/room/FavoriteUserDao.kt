package com.example.githubapi.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.githubapi.data.local.entity.FavoriteUserEntity

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUserEntity: FavoriteUserEntity)

    @Delete
    fun delete(favoriteUserEntity: FavoriteUserEntity)

    @Update
    fun update(favoriteUserEntity: FavoriteUserEntity)

    @Query("SELECT * FROM FavoriteUserEntity ORDER BY username ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUserEntity>>?


    @Query("SELECT EXISTS (SELECT * FROM FavoriteUserEntity WHERE username = :username AND isFavorite = 1)")
    fun isFavoriteUser(username: String): LiveData<Boolean>

}