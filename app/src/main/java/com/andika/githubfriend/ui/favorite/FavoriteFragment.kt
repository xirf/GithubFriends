package com.andika.githubfriend.ui.favorite

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.andika.githubfriend.database.FavoriteUser
import com.andika.githubfriend.databinding.FragmentFavoriteBinding
import com.andika.githubfriend.preferences.SettingPreferences
import com.andika.githubfriend.preferences.dataStore
import com.andika.githubfriend.ui.userdetail.UserDetailActivity
import com.andika.githubfriend.viewmodels.FavoriteUserViewModel
import com.andika.githubfriend.viewmodels.ViewModelFactory


class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteUserViewModel
    private val adapter: FavoriteUserAdapter = FavoriteUserAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val factory = ViewModelFactory(pref, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[FavoriteUserViewModel::class.java]

        viewModel.getAll().observe(viewLifecycleOwner) { favoriteUser ->
            if (!favoriteUser.isNullOrEmpty()) {
                adapter.setData(favoriteUser as ArrayList<FavoriteUser>)
                binding.rvUser.visibility = View.VISIBLE
                binding.emptyLayout.emptyScreen.visibility = View.GONE
            } else {
                binding.rvUser.visibility = View.GONE
                binding.emptyLayout.emptyScreen.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.rvUser.visibility = View.VISIBLE
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback {
            val intent = Intent(requireContext(), UserDetailActivity::class.java)
            intent.putExtra(UserDetailActivity.EXTRA_USERNAME, it.username)
            intent.putExtra(UserDetailActivity.EXTRA_TYPE, it.type)
            startActivity(intent)
        }
    }
}