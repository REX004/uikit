package mad.training.uikit_show

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mad.training.uikit_show.databinding.ActivityMainShowCaseBinding

/**
 * Описание назначения класса: Главный экран для демонстрации компонентов UI Kit.
 * Автор создания: Твое Имя
 */
class MainShowCaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainShowCaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainShowCaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        binding.buttonShowcaseInputs.setOnClickListener {
            startActivity(Intent(this, InputFieldShowcaseActivity::class.java))
        }
        binding.buttonShowcaseSelects.setOnClickListener {
            startActivity(Intent(this, SelectShowcaseActivity::class.java))
        }
        binding.buttonShowcaseSearch.setOnClickListener {
            startActivity(Intent(this, SearchShowcaseActivity::class.java))
        }
        binding.buttonShowcaseButtons.setOnClickListener {
            startActivity(Intent(this, ButtonShowcaseActivity::class.java))
        }
    }
}