package mad.training.uikit_show

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import mad.training.uikit.R
import mad.training.uikit_show.databinding.ActivityInputFieldShowcaseBinding

class InputFieldShowcaseActivity : AppCompatActivity() {
    private val binding: ActivityInputFieldShowcaseBinding by lazy {
        ActivityInputFieldShowcaseBinding.inflate(
            layoutInflater
        )
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.checkBt.setOnClickListener {
            binding.inputLayoutError.error = "Это обязательное поле (пример ошибки)"
            binding.inputLayoutError.isErrorEnabled = true
            binding.inputLayoutError.editText?.setBackgroundResource(R.drawable.uikit_edit_text_background_error)

        }
    }
}