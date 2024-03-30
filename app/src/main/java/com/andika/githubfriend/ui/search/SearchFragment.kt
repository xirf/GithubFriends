package com.andika.githubfriend.ui.search

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
import com.andika.githubfriend.databinding.FragmentSearchBinding
import com.andika.githubfriend.preferences.SettingPreferences
import com.andika.githubfriend.preferences.dataStore
import com.andika.githubfriend.ui.userdetail.UserDetailActivity
import com.andika.githubfriend.viewmodels.MainViewModel
import com.andika.githubfriend.viewmodels.ViewModelFactory

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: MainViewModel
    private val adapter: UserAdapter by lazy { UserAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(pref, requireActivity().application)
        )[MainViewModel::class.java]

        checkSearchResult()
        setupSearchView()
        loadingObserver()

        return binding.root
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                viewModel.searchUser(searchView.text.toString())
                searchView.hide()
                checkSearchResult()
                false
            }
        }
    }


    private fun checkSearchResult() {
        viewModel.searchList.observe(viewLifecycleOwner) { searchResult ->
            if (!searchResult.isNullOrEmpty()) {
                binding.emptyLayout.emptyScreen.visibility = View.GONE
                binding.rvUser.visibility = View.VISIBLE
                adapter.setData(searchResult)
                renderRecyclerView()
            } else {
                binding.emptyLayout.emptyScreen.visibility = View.VISIBLE
                binding.rvUser.visibility = View.GONE
            }
        }
    }

    private fun renderRecyclerView() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.rvUser.visibility = View.VISIBLE
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback {
            val intent = Intent(requireContext(), UserDetailActivity::class.java)
            intent.putExtra(UserDetailActivity.EXTRA_USERNAME, it.login)
            intent.putExtra(UserDetailActivity.EXTRA_TYPE, it.type)
            startActivity(intent)
        }
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
}