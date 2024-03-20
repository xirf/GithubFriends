package com.andika.octofriends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andika.octofriends.databinding.ItemUserBinding
import com.andika.octofriends.model.Items
import com.andika.octofriends.model.UserFollowersResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class FollowAdapter : RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {

    private val listUser = ArrayList<UserFollowersResponse>()
    private lateinit var onItemClickCallback: FollowAdapter.OnItemClickCallback

    class ListViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserFollowersResponse) {

            with(binding) {
                tvName.text = user.login
                tvUsername.text = user.htmlUrl
                tvType.text = user.type
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(ivAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
    }

    fun setData(items: ArrayList<UserFollowersResponse>) {
        val isDifferent = DiffUtil.calculateDiff(DiffUtilCallback(listUser, items))
        listUser.clear()
        listUser.addAll(items)
        isDifferent.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(data: UserFollowersResponse)
    }

    class DiffUtilCallback(
        private val oldList: List<UserFollowersResponse>,
        private val newList: List<UserFollowersResponse>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.hashCode() == newItem.hashCode()
        }
    }

}