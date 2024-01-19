package com.example.githubapiv3.ui.user_repository

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapiv3.R
import com.example.githubapiv3.databinding.RepoItemLayoutBinding
import com.example.githubapiv3.domain.models.Repository

class UserRepositoriesAdapter (
    private val listener: (Repository) -> Unit ) :
    RecyclerView.Adapter<UserRepositoriesAdapter.ViewHolder>() {

    private lateinit var mContext: Context
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Repository>() {

        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repo_item_layout, parent, false)

        mContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = differ.currentList[position]
        holder.itemView.setOnClickListener {
            listener(repo)
        }
        holder.bind(repo)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = differ.currentList.size

    fun addItems(items: List<Repository>){
        differ.submitList(items)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = RepoItemLayoutBinding.bind(view)

        fun bind(repo: Repository) = with(binding){
            repo.apply {
                nameValue.text = name
                languageValue.text = language
                descriptionValue.text = description
                starsValue.text = stars.toString()
            }
        }
    }
}