package com.example.githubapi.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.R
import com.example.githubapi.data.response.User
import com.example.githubapi.databinding.FragmentTabLayoutBinding

class TabLayoutFragment : Fragment() {

    companion object {
        const val ARG_SECTION_NUMBER = "arg_section_number"
        const val USERNAME = "username"
    }

    private lateinit var fragmentTabLayoutBinding: FragmentTabLayoutBinding
    private val detailViewModel by activityViewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTabLayoutBinding = FragmentTabLayoutBinding.inflate(inflater, container, false)
        return fragmentTabLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayoutIndex = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(USERNAME)

        detailViewModel.getUserFollowerList(username!!)
        detailViewModel.getUserFollowingList(username)
        if (tabLayoutIndex == 0) {
            detailViewModel.listFollowers.observe(viewLifecycleOwner) { listFollowers ->
                if (listFollowers.isNullOrEmpty()) {
                    val message = getString(R.string.followers_not_found)
                    fragmentTabLayoutBinding.tabLayoutRecyclerView.visibility = View.GONE
                    fragmentTabLayoutBinding.tvTabLayoutMessage?.visibility = View.VISIBLE
                    fragmentTabLayoutBinding.tvTabLayoutMessage?.text = message
                } else {
                    showTabLayoutRecyclerView(listFollowers)
                }
            }

        } else {
            detailViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
                if (listFollowing.isNullOrEmpty()) {
                    val message = getString(R.string.following_not_found)
                    fragmentTabLayoutBinding.tabLayoutRecyclerView.visibility = View.GONE
                    fragmentTabLayoutBinding.tvTabLayoutMessage?.visibility = View.VISIBLE
                    fragmentTabLayoutBinding.tvTabLayoutMessage?.text = message
                } else {
                    showTabLayoutRecyclerView(listFollowing)
                }
            }
        }

    }

    private fun showTabLayoutRecyclerView(list: List<User>) {
        val rvAdapter = TabLayoutAdapter()
        rvAdapter.submitList(list)
        fragmentTabLayoutBinding.tabLayoutRecyclerView.adapter = rvAdapter
        fragmentTabLayoutBinding.tabLayoutRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

}