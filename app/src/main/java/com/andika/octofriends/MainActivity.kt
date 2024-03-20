package com.andika.octofriends

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.andika.octofriends.adapter.UserAdapter
import com.andika.octofriends.databinding.ActivityMainBinding
import com.andika.octofriends.model.Items
import com.andika.octofriends.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val adapter: UserAdapter = UserAdapter()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getLoadingState.observe(this, this::showLoading)
        viewModel.searchUser("xirf")

        showViewModel()
        showRecycleView()

        binding.tvNotFound.visibility = View.GONE
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showViewModel() {
        viewModel.getSearchResults.observe(this) { searchList ->
            if (searchList != null && searchList.size != 0) {
                binding.tvNotFound.visibility = View.GONE
                binding.rvUser.visibility = View.VISIBLE
                adapter.setData(searchList)
            } else {
                binding.tvNotFound.visibility = View.VISIBLE
                binding.rvUser.visibility = View.GONE
            }
        }
    }

    private fun showRecycleView() {
        binding.rvUser.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvUser.visibility = View.VISIBLE
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback{user: Items ->
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra(UserActivity.EXTRA_USER, user.login)
            intent.putExtra(UserActivity.EXTRA_TYPE, user.type)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val close = menu.findItem(R.id.search)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        close.icon?.setVisible(false, false)

        return true
    }

}