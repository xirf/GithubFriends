package com.andika.githubfriend.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andika.githubfriend.databinding.FragmentProfileBinding
import com.andika.githubfriend.preferences.SettingPreferences
import com.andika.githubfriend.preferences.dataStore
import com.andika.githubfriend.viewmodels.MainViewModel
import com.andika.githubfriend.viewmodels.ViewModelFactory
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(pref, requireActivity().application)
        )[MainViewModel::class.java]


        if (viewModel.userDetail.value == null) {
            viewModel.getUserDetail("xirf")
        }

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchDarkMode.isChecked = isDarkModeActive
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSettings(isChecked)
        }
        loadingObserver()
        renderProfile()
        return binding.root
    }

    private fun loadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingLayout.loadingScreen.visibility = View.VISIBLE
            } else {
                binding.loadingLayout.loadingScreen.visibility = View.GONE
            }
        }
    }

    private fun renderProfile() {
        viewModel.userDetail.observe(viewLifecycleOwner) { userDetail ->
            if (userDetail != null) {
                with(binding.header) {
                    Glide.with(this@ProfileFragment).load(userDetail.avatarUrl).circleCrop()
                        .into(civAvatar)
                    tvName.text = userDetail.name
                    tvUsername.text = userDetail.login
                    tvWork.text = userDetail.company
                    tvLocation.text = userDetail.location
                    tvDesc.text = userDetail.bio
                    tvRepos.text = userDetail.publicRepos
                    tvFollowers.text = userDetail.followers
                    tvFollowing.text = userDetail.following
                    btnShare.visibility = View.GONE
                    btnAddFavorite.visibility = View.GONE
                }
            }
        }
    }
}