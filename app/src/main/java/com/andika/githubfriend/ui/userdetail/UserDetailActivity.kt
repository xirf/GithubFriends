package com.andika.githubfriend.ui.userdetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.andika.githubfriend.R
import com.andika.githubfriend.database.FavoriteUser
import com.andika.githubfriend.databinding.ActivityUserDetailBinding
import com.andika.githubfriend.model.UserDetailResponse
import com.andika.githubfriend.preferences.SettingPreferences
import com.andika.githubfriend.preferences.dataStore
import com.andika.githubfriend.viewmodels.FavoriteUserViewModel
import com.andika.githubfriend.viewmodels.MainViewModel
import com.andika.githubfriend.viewmodels.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var favUserViewModel: FavoriteUserViewModel
    private var inFav: Boolean = false
    private var shareText: String = String()
    private var toast: Toast? = null

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_TYPE = "extra_type"
        var username = String()
        var type = String()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupViewModels()
        setupUsernameAndType()
        setupUserDetailAdapter()
        setupActionBar()
        setupFavoriteUserObserver()

        loadingObserver()
        renderGivenData()
    }

    private fun setupViewModels() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(pref, application)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        favUserViewModel = ViewModelProvider(this, factory)[FavoriteUserViewModel::class.java]
    }

    private fun setupUsernameAndType() {
        username = intent.getStringExtra(EXTRA_USERNAME).toString()
        type = intent.getStringExtra(EXTRA_TYPE).toString()
    }

    private fun setupUserDetailAdapter() {
        val userDetailAdapter = UserDetailAdapter(this)
        binding.viewPager.adapter = userDetailAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                1 -> "Following"
                else -> throw IllegalArgumentException("Unknown tab position: $position")
            }
        }.attach()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = username
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun setupFavoriteUserObserver() {
        favUserViewModel.checkFavoriteUser(username).observe(this) { isFavorite ->
            inFav = isFavorite
            val color = if (isFavorite) {
                R.color.colorInfo
            } else {
                R.color.colorNeutralVariant
            }

            with(binding.toolbar) {
                btnAddFavorite.setBackgroundColor(resources.getColor(color, theme))
            }

        }
    }


    private fun renderGivenData() {
        viewModel.getUserDetail(username)
        viewModel.userDetail.observe(this) { detailUser ->
            detailUser?.let {
                setupToolbar(it)
                setupShareButton(it)
                setupFavoriteButton(it)
            }
        }

        setupBackButton()
        setupToolbarTitle()
    }

    private fun setupFavoriteButton(detailUser: UserDetailResponse) {
        with(binding.toolbar) {
            btnAddFavorite.setOnClickListener {
                val favoriteUser = FavoriteUser(
                    username = detailUser.login,
                    name = detailUser.name ?: "no name",
                    type = detailUser.type,
                    pictureUrl = detailUser.avatarUrl
                )
                if (inFav) {
                    favUserViewModel.delete(favoriteUser)
                    showToast("$username dihapus dari favorit")
                } else {
                    favUserViewModel.insert(favoriteUser)
                    showToast("$username ditambahkan ke favorit")
                }
            }
        }
    }

    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun setupToolbar(detailUser: UserDetailResponse) {
        with(binding.toolbar) {
            Glide.with(this@UserDetailActivity)
                .load(detailUser.avatarUrl)
                .circleCrop()
                .into(civAvatar)

            if (detailUser.company.isNullOrEmpty()) {
                tvWork.visibility = View.GONE
            }
            if (detailUser.location.isNullOrEmpty()) {
                tvLocation.visibility = View.GONE
            }
            if (detailUser.bio.isNullOrEmpty()) {
                tvDesc.visibility = View.GONE
            }

            tvUsername.text = detailUser.login
            tvName.text = detailUser.name ?: detailUser.login
            tvLocation.text = detailUser.location
            tvDesc.text = detailUser.bio
            tvWork.text = detailUser.company
            tvFollowers.text = detailUser.followers
            tvFollowing.text = detailUser.following
            tvRepos.text = detailUser.publicRepos
        }
    }

    private fun setupShareButton(detailUser: UserDetailResponse) {
        with(binding.toolbar) {
            shareText = "Check out ${detailUser.name} on GitHub\n\n${detailUser.htmlUrl}"

            btnShare.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }


    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupToolbarTitle() {
        binding.tvTitleToolbar.text = username
    }


    private fun loadingObserver() {
        viewModel.isLoading.observe(this) {
            if (it) {
                binding.loadingLayout.loadingScreen.visibility = View.VISIBLE
            } else {
                binding.loadingLayout.loadingScreen.visibility = View.GONE
            }
        }
    }
}