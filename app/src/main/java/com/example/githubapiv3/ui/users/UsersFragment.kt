package com.example.githubapiv3.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapiv3.R
import com.example.githubapiv3.data.api.RetrofitClient
import com.example.githubapiv3.databinding.FragmentUsersBinding
import com.example.githubapiv3.domain.models.User
import com.example.githubapiv3.domain.repository.UserRepositoryImpl
import com.example.githubapiv3.utils.Resource
import com.example.githubapiv3.utils.mappers.RepositoryListMapper
import com.example.githubapiv3.utils.mappers.RepositoryMapper
import com.example.githubapiv3.utils.mappers.UserDetailsMapper
import com.example.githubapiv3.utils.mappers.UserListMapper
import com.example.githubapiv3.utils.mappers.UserMapper
import com.example.githubapiv3.utils.visible


class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var _adapter: UserAdapter
    private val _viewModel: UserViewModel by activityViewModels<UserViewModel>(
       factoryProducer = {
           UsersViewModelFactory(
               repo = UserRepositoryImpl(
                   api = RetrofitClient.getInstance(),
                   userMapperList = UserListMapper(UserMapper),
                   userDetailsMapper = UserDetailsMapper,
                   repositoryMapperList = RepositoryListMapper(RepositoryMapper)
               )
           )
       }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        setupUI()
    }

    private fun setupUI() = with(binding){
        initRecyclerView()

        _viewModel.users.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingData()
                }
                is Resource.Success -> {
                    _adapter.addItems(it.data)
                    loadingData(false)
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(),R.string.get_users_error,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadingData(isLoading: Boolean = true) = with(binding) {
        progressBar.visible = isLoading
        recyclerview.visible = !isLoading
    }

    private fun initRecyclerView() = with(binding.recyclerview) {
        addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        layoutManager = LinearLayoutManager(requireContext())
        _adapter = UserAdapter(::onClickListener)
        adapter = _adapter
    }

    private fun onClickListener(user: User){
        val action = UsersFragmentDirections.actionNavigationUsersToRepositories(user)
        findNavController().navigate(action)
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider( object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.users_menu, menu)
                val menuItem = menu.findItem(R.id.action_search)
                val searchView = menuItem.actionView as SearchView?
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        (binding.recyclerview.adapter as Filterable).filter.filter(newText)
                        return false
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.users_menu,menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerview.adapter = null
        _binding = null
    }
}