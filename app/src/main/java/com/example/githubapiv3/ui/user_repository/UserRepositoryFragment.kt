package com.example.githubapiv3.ui.user_repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.githubapiv3.R
import com.example.githubapiv3.databinding.FragmentUserRepositoryBinding
import com.example.githubapiv3.domain.models.Repository
import com.example.githubapiv3.ui.users.UserViewModel
import com.example.githubapiv3.utils.Resource
import com.example.githubapiv3.utils.visible

class UserRepositoryFragment : Fragment() {

    private var _binding: FragmentUserRepositoryBinding? = null
    private val binding get() = _binding!!
    private val _viewModel by activityViewModels<UserViewModel>()
    private lateinit var _adapter: UserRepositoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserRepositoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _viewModel.setUserSelected(navArgs<UserRepositoryFragmentArgs>().value.user)

        setupUI()
        _viewModel.selectedUserDetails.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading ->
                    binding.userDetailed.userDetailsProgressBar.visible = true
                is Resource.Success -> {
                    binding.userDetailed.userDetailsProgressBar.visible = false
                    binding.userDetailed.apply {
                        fullname.text = it.data.name
                        followersValue.text = it.data.followers
                        followingValue.text = it.data.following
                    }
                }

                is Resource.Error -> {
                    binding.userDetailed.userDetailsProgressBar.visible = false
                    Toast.makeText(
                        requireContext(),
                        R.string.get_user_details_error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        _viewModel.selectedUserRepositories.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> loadingData()
                is Resource.Success -> {
                    _adapter.addItems(it.data)
                    loadingData(false)
                }
                is Resource.Error -> {
                    binding.progressBar.visible = false
                    Toast.makeText(
                        requireContext(),
                        R.string.get_user_repositories_error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        _viewModel.getUserDetails()
        _viewModel.getUserRepositories()
    }

    private fun loadingData(isLoading: Boolean = true) = with(binding) {
        progressBar.visible = isLoading
        recyclerview.visible = !isLoading
    }

    private fun setupUI() = with(binding) {
        Glide.with(requireContext())
            .load(_viewModel.selectedUser.avatar)
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(userDetailed.image)

        userDetailed.username.text = String.format(
            requireContext().getString(R.string.username),
            _viewModel.selectedUser.username
        )

        initRecyclerView()
    }

    private fun initRecyclerView() = with(binding.recyclerview) {
        layoutManager = LinearLayoutManager(activity)
        addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        _adapter = UserRepositoriesAdapter(::onClickListener)
        adapter = _adapter
    }

    private fun onClickListener(repository: Repository) {
        val action =
            UserRepositoryFragmentDirections.actionNavigationRepositoriesToRepository(repository.url)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerview.adapter = null
        _binding = null
    }
}