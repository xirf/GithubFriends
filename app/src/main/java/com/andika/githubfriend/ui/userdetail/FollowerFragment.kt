package com.andika.githubfriend.ui.userdetail

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
import com.andika.githubfriend.adapter.UserAdapter
import com.andika.githubfriend.databinding.FragmentFollowerBinding
import com.andika.githubfriend.model.Items
import com.andika.githubfriend.model.UserFollowersResponse
import com.andika.githubfriend.preferences.SettingPreferences
import com.andika.githubfriend.preferences.dataStore
import com.andika.githubfriend.viewmodels.MainViewModel
import com.andika.githubfriend.viewmodels.ViewModelFactory

class FollowerFragment(private val type: String) : Fragment() {

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var viewModel: MainViewModel
    private val adapter: UserAdapter = UserAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(layoutInflater)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(
            this, ViewModelFactory(pref, requireActivity().application)
        )[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadingObserver()
    }

    private fun setupRecyclerView() {
        binding.rvUser.adapter = adapter
        if (type == "followers") {
            viewModel.getUserFollowers(UserDetailActivity.username)
            viewModel.userFollowers.observe(viewLifecycleOwner) { followers ->
                if (!followers.isNullOrEmpty()) {
                    renderer(convertToItems(followers))
                }
            }
        } else {
            viewModel.getUserFollowings(UserDetailActivity.username)
            viewModel.userFollowing.observe(viewLifecycleOwner) { following ->
                if (!following.isNullOrEmpty()) {
                    renderer(convertToItems(following))
                }
            }
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(context, 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(context)
        }

        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback { items -> selectedUser(items) }
    }


    private fun selectedUser(data: Items) {
        val intent = Intent(activity, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_USERNAME, data.login)
        intent.putExtra(UserDetailActivity.EXTRA_TYPE, data.type)
        startActivity(intent)
    }

    private fun convertToItems(following: ArrayList<UserFollowersResponse>): ArrayList<Items> {
        val listItems = ArrayList<Items>()
        for (item in following) {
            val items = Items(
                login = item.login,
                avatarUrl = item.avatarUrl,
                htmlUrl = item.htmlUrl,
                type = item.type
            )
            listItems.add(items)
        }
        return listItems
    }

    private fun renderer(follow: ArrayList<Items>?) {
        if (!follow.isNullOrEmpty()) {
            binding.emptyLayout.emptyScreen.visibility = View.GONE
            binding.rvUser.visibility = View.VISIBLE
            adapter.setData(follow)
        } else {
            binding.emptyLayout.emptyScreen.visibility = View.VISIBLE
            binding.rvUser.visibility = View.GONE
        }
    }

    private fun loadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.loadingLayout.loadingScreen.visibility = View.VISIBLE
            } else {
                binding.loadingLayout.loadingScreen.visibility = View.GONE
            }
        }
    }
}