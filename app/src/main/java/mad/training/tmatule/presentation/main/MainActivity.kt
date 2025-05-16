package mad.training.tmatule.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import mad.training.tmatule.R
import mad.training.tmatule.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var navController: NavController = NavController(this)
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        handleInitialNavigation(savedInstanceState)

        setupBottomNavVisibility()
    }

    companion object {
        private const val EXTRA_DESTINATION = "extra_destination"
        fun createIntent(context: Context, destination: String): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_DESTINATION, destination)
            }
        }
    }

    private fun setupBottomNavVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_main,
                R.id.navigation_catalog,
                R.id.navigation_profile,
                R.id.navigation_projects -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
            }
        }
    }

    private fun handleInitialNavigation(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val destination = intent.getStringExtra(EXTRA_DESTINATION)
            when (destination) {
                "home" -> navController.navigate(R.id.createPassAccFragment)
                "auth" -> navController.navigate(R.id.createPassAccFragment)
            }
        }
    }
}