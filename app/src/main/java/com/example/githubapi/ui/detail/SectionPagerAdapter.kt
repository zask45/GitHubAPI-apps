package com.example.githubapi.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        val toBeInflatedFragment = TabLayoutFragment()
        toBeInflatedFragment.arguments = Bundle().apply {
            putInt(TabLayoutFragment.ARG_SECTION_NUMBER, position)
            putString(TabLayoutFragment.USERNAME, username)
        }

        return toBeInflatedFragment
    }

    override fun getItemCount(): Int = 2

}