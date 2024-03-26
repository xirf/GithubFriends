package com.andika.octofriends

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.andika.octofriends.adapter.FollowAdapter
import com.andika.octofriends.databinding.FragmentFollowingBinding
import com.andika.octofriends.model.UserFollowersResponse
import com.andika.octofriends.viewmodel.MainViewModel

class FollowingFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val adapter = FollowAdapter()
    private lateinit var binding: FragmentFollowingBinding
    private val _binding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecycleView()
        showViewModel()
        viewModel.getLoadingState.observe(viewLifecycleOwner, this::showLoading)
    }

    private fun showViewModel() {
        viewModel.getUserFollowings(UserActivity.username)
        viewModel.getUserFollowings.observe(viewLifecycleOwner) { userFollowing ->
            if (userFollowing == null || userFollowing.isEmpty()) {
                binding.tvNotFound.visibility = View.VISIBLE
                binding.rvFollowers.visibility = View.GONE

                if (UserActivity.type == "Organization") {
                    binding.tvNotFound.text = resources.getString(R.string.no_following_org)
                } else {
                    binding.tvNotFound.text = resources.getString(R.string.no_following)
                }

            } else {
                adapter.setData(userFollowing)
            }
        }
    }

    private fun showRecycleView() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvFollowers.layoutManager = GridLayoutManager(activity, 2)
        } else {
            binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        }
        binding.rvFollowers.setHasFixedSize(true)
        binding.rvFollowers.adapter = adapter
        adapter.setOnItemClickCallback { items -> selectedUser(items) }
    }

    private fun selectedUser(data: UserFollowersResponse) {
        val intent = Intent(activity, UserActivity::class.java)
        intent.putExtra(UserActivity.EXTRA_USER, data.login)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserFollowings(UserActivity.username)
    }
}