package com.andika.octofriends

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.andika.octofriends.adapter.UserDetailAdapter
import com.andika.octofriends.databinding.ActivityUserBinding
import com.andika.octofriends.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator

class UserActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityUserBinding

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_TYPE = "extra_type"
        var username = String()
        var type = String()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(EXTRA_USER).toString()
        type = intent.getStringExtra(EXTRA_TYPE).toString()

        val organizationTitle = resources.getString(R.string.organization)
        val userTitle = resources.getString(R.string.user)

        supportActionBar?.title = if (type == "Organization") organizationTitle else userTitle
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userDetailAdapter = UserDetailAdapter(this)
        binding.viewPager.adapter = userDetailAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(
                when (position) {
                    0 -> R.string.text_followers
                    else -> R.string.text_following
                }
            )
        }.attach()

        viewModel.getUserDetail(username)
        showViewModel()
        viewModel.getLoadingState.observe(this, this::showLoading)
    }

    private fun showViewModel() {
        viewModel.detailUser(username)
        viewModel.getUserDetail.observe(this) { detailUser ->
            with(binding) {
                Glide.with(this@UserActivity).load(detailUser.avatarUrl)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(ivAvatar)


                if (detailUser.bio.isNullOrEmpty()) {
                    tvBio.visibility = View.GONE
                }
                if (detailUser.location.isNullOrEmpty()) {
                    tvLocation.visibility = View.GONE
                }
                if (detailUser.company.isNullOrEmpty()) {
                    tvCompany.visibility = View.GONE
                }

                tvName.text = detailUser.name ?: resources.getString(R.string.no_name)
                tvBio.text = detailUser.bio
                tvUsername.text = detailUser.login
                tvLocation.text = detailUser.location
                tvCompany.text = detailUser.company
                tvFollowers.text = detailUser.followers
                tvFollowing.text = detailUser.following

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }




}