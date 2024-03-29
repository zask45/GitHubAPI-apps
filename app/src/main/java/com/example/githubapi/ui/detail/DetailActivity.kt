package com.example.githubapi.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapi.R
import com.example.githubapi.databinding.ActivityDetailBinding
import com.example.githubapi.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var activityDetailBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)

        onBackIconPressed()

        val detailViewModel = obtainViewModel(this)

        detailViewModel.isLoading.observe(this) { isLoading ->
            activityDetailBinding.detailProgressBar.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

        val username = intent.getStringExtra("username")
        detailViewModel.getUserData(username!!)

        detailViewModel.isFavorite(username).observe(this) { isFavorite ->
            if (isFavorite) {
                activityDetailBinding.fabAdd.setImageResource(R.drawable.fab_baseline_favorite_filled)
            } else {
                activityDetailBinding.fabAdd.setImageResource(R.drawable.fab_baseline_favorite_border)

            }
        }

        detailViewModel.user.observe(this) { user ->

            activityDetailBinding.fabAdd.setOnClickListener {

                try {
                    if (user.isFavorite) {
                        detailViewModel.delete(user)
                        detailViewModel.setFavoriteUser(user, false)
                    } else {
                        detailViewModel.insert(user)
                        detailViewModel.setFavoriteUser(user, true)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }

            }

        }


        detailViewModel.avatarId.observe(this) { avatarImage ->
            Glide.with(activityDetailBinding.root)
                .load(avatarImage)
                .into(activityDetailBinding.ivDetailProfile)
        }

        detailViewModel.name.observe(this) { name ->
            activityDetailBinding.tvDetailName.text = name
        }

        detailViewModel.username.observe(this) { uname ->
            activityDetailBinding.tvDetailUsername.text = uname
        }

        detailViewModel.followers.observe(this) { followers ->
            activityDetailBinding.tvDetailFollowers.text = buildString {
                append("Followers ")
                append(followers.toString())
            }
        }

        detailViewModel.following.observe(this) { following ->
            activityDetailBinding.tvDetailFollowing.text = buildString {
                append("Following ")
                append(following.toString())
            }

        }

        inflateTabLayout(username)


    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)

        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun inflateTabLayout(username: String) {
        val sectionPagerAdapter = SectionPagerAdapter(this@DetailActivity, username)
        val viewPager: ViewPager2 = activityDetailBinding.viewPager
        viewPager.adapter = sectionPagerAdapter

        val tabs = activityDetailBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun onBackIconPressed() {
        activityDetailBinding.appBar.setNavigationOnClickListener {
            this.finish()
        }
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_title_1,
            R.string.tab_title_2
        )
    }
}