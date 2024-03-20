package com.andika.octofriends.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andika.octofriends.FollowerFragment
import com.andika.octofriends.FollowingFragment

class UserDetailAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int) = when (position) {
        0 -> FollowerFragment()
        1 -> FollowingFragment()
        else -> FollowerFragment()
    }
}