package com.example.githubapiv3.ui.users

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.githubapiv3.R
import com.example.githubapiv3.databinding.UserItemLayoutBinding
import com.example.githubapiv3.domain.models.User
import java.util.Locale

class UserAdapter(
    private val _listener: (User) -> Unit
) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>(), Filterable {

    lateinit var _context: Context
    var _itemsAll: List<User> = listOf()

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
    private val _differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item_layout, parent, false)

        _context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = _differ.currentList[position]
        holder.itemView.setOnClickListener {
            _listener(user)
        }
        holder.bind(user)
    }

    override fun getItemCount() = _differ.currentList.size

    fun addItems(items: List<User>) {
        _itemsAll = items
        _differ.submitList(items)
    }

    private var _filter: Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredList: MutableList<User> = ArrayList()
            if (charSequence.isEmpty()) {
                filteredList.addAll(_itemsAll)
            } else {
                filteredList.addAll(_itemsAll.filter {
                    it.username.lowercase(Locale.getDefault())
                        .contains(
                            charSequence.toString()
                                .lowercase(Locale.getDefault())
                        )
                })
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        @SuppressLint("NotifyDataSetChanged")
        @Suppress("UNCHECKED_CAST")
        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            _differ.submitList(filterResults.values as List<User>)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = UserItemLayoutBinding.bind(view)

        fun bind(user: User) = with(binding) {
            user.apply {
                Glide.with(_context)
                    .load(user.avatar)
                    .centerInside()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image)
            }
            username.text = String.format(_context.getString(R.string.username), user.username)
        }
    }

    override fun getFilter() = _filter
}