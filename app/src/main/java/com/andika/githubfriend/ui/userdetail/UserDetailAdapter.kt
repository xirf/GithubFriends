package com.andika.githubfriend.ui.userdetail

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class UserDetailAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int) = when (position) {
        0 -> FollowerFragment("followers")
        else -> FollowerFragment("following")
    }
}