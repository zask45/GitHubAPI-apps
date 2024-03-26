package com.example.githubapi.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapi.R
import com.example.githubapi.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_title_1,
            R.string.tab_title_2
        )
    }

    private lateinit var activityDetailBinding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)

        onBackIconPressed()

        detailViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                activityDetailBinding.detailProgressBar?.visibility  = View.VISIBLE
            } else {
                activityDetailBinding.detailProgressBar?.visibility = View.GONE
            }
        }

        val username = intent.getStringExtra("username")
        detailViewModel.getUserData(username!!)
        showUserData(username)
    }

    private fun showUserData(username: String) {

        detailViewModel.avatarId.observe(this) {avatarImage ->
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
        activityDetailBinding.appBar?.setNavigationOnClickListener {
            this.finish()
        }
    }
}