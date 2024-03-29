package com.example.githubapi.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapi.data.local.entity.FavoriteUserEntity
import com.example.githubapi.data.remote.response.User
import com.example.githubapi.data.remote.response.UserDetailResponse
import com.example.githubapi.data.remote.retrofit.ApiConfig
import com.example.githubapi.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _avatarId = MutableLiveData<String>()
    val avatarId: LiveData<String> = _avatarId

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _id = MutableLiveData<Int>()
    val id: LiveData<Int> = _id

    private val _followers = MutableLiveData<Int>()
    val followers: LiveData<Int> = _followers

    private val _following = MutableLiveData<Int>()
    val following: LiveData<Int> = _following

    private val _user = MutableLiveData<FavoriteUserEntity>()
    val user: LiveData<FavoriteUserEntity> = _user

    private val _listFollowers = MutableLiveData<List<User>>()
    val listFollowers: LiveData<List<User>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<User>>()
    val listFollowing: LiveData<List<User>> = _listFollowing

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getUserData(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailUser(username)
        Log.d(TAG, "$client")
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _avatarId.value = response.body()?.avatarUrl
                    _username.value = response.body()?.login
                    _name.value = response.body()?.name
                    _id.value = response.body()?.id
                    _followers.value = response.body()?.followers
                    _following.value = response.body()?.following

                    _user.value = FavoriteUserEntity(
                        username,
                        avatarId.value!!,
                        id.value!!,
                        isFavorite(username).value ?: false
                    )

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getUserFollowerList(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _listFollowers.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserFollowingList(username: String) {

        _isLoading.value = true

        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                    _listFollowing.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun insert(favoriteUser: FavoriteUserEntity) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUserEntity) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

   fun isFavorite(username: String): LiveData<Boolean> = mFavoriteUserRepository.isFavorite(username)

    fun setFavoriteUser(favoriteUser: FavoriteUserEntity, favoriteState: Boolean) = mFavoriteUserRepository.setFavoriteUser(favoriteUser, favoriteState)


    companion object {
        private const val TAG = "DetailViewModel"
    }

}