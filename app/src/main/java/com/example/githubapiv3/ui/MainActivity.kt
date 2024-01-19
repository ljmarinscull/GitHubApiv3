package com.example.githubapiv3.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.githubapiv3.R
import com.example.githubapiv3.databinding.ActivityMainBinding
import com.example.githubapiv3.utils.visible
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var _navController: NavController
    private lateinit var _navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _navView = binding.navView
        _navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_users
            )
        )
        setupActionBarWithNavController(_navController, appBarConfiguration)
        _navView.setupWithNavController(_navController)

        setupBottomNavVisibility()
    }

    private fun bottomBarVisibility(isVisible: Boolean = true) {
        _navView.visible = isVisible
    }

    private fun setupBottomNavVisibility(){
        _navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id){
                R.id.navigation_webview -> bottomBarVisibility(false)
                R.id.navigation_repository -> bottomBarVisibility(false)
                else -> bottomBarVisibility()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return _navController.navigateUp() || super.onSupportNavigateUp()
    }
}