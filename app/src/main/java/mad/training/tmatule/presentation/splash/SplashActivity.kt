package mad.training.tmatule.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mad.training.tmatule.databinding.ActivitySplashBinding
import mad.training.tmatule.presentation.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            delay(2000)
            navigateToMain()
        }

    }

    private fun navigateToMain() {
        startActivity(MainActivity.createIntent(this, destination = "auth"))
    }
}