package mad.training.tmatule.presentation.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import mad.training.tmatule.R
import mad.training.tmatule.databinding.FragmentLoginBinding
import mad.training.tmatule.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInputListeners()
        updateButtonState()
        updateTextInputLayoutAppearance(
            binding.emailTil,
            binding.emailEt.text.isNullOrEmpty(),
            binding.emailTil.hasFocus()
        )
        updateTextInputLayoutAppearance(
            binding.passwordTil,
            binding.passwordEt.text.isNullOrEmpty(),
            binding.passwordTil.hasFocus()
        )
    }

    override fun observeViewModel() {
        super.observeViewModel()
        // Здесь будешь наблюдать за LiveData из ViewModel, например, для показа ошибок валидации
        viewModel.emailError.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                binding.emailTil.error = errorMessage
                // Устанавливаем фон и обводку для ошибки
                binding.emailTil.boxBackgroundColor = ContextCompat.getColor(
                    requireContext(),
                    mad.training.uikit.R.color.uikit_keypad_button_background_default
                )
                // Обводка уже должна стать красной из-за setError, но можно подстраховаться
                // binding.emailTil.boxStrokeColor = ContextCompat.getColor(requireContext(), R.color.error)
            } else {
                binding.emailTil.error = null
                binding.emailTil.isErrorEnabled = false // Важно для сброса состояния ошибки
                // Возвращаем обычный фон и обводку
                binding.emailTil.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), mad.training.uikit.R.color.input_bg)
                updateTextInputLayoutAppearance(
                    binding.emailTil,
                    binding.emailEt.text.isNullOrEmpty(),
                    binding.emailTil.hasFocus()
                )
            }
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                binding.passwordTil.error = errorMessage
                binding.passwordTil.boxBackgroundColor = ContextCompat.getColor(
                    requireContext(),
                    mad.training.uikit.R.color.uikit_keypad_button_background_default
                )
            } else {
                binding.passwordTil.error = null
                binding.passwordTil.isErrorEnabled = false
                binding.passwordTil.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), mad.training.uikit.R.color.input_bg)
                updateTextInputLayoutAppearance(
                    binding.passwordTil,
                    binding.passwordEt.text.isNullOrEmpty(),
                    binding.passwordTil.hasFocus()
                )
            }
        }
    }

    override fun applyClick() {
        super.applyClick()
        binding.nextBt.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            viewModel.loginUser(email, password)
            findNavController().navigate(R.id.createPassAccFragment)
        }

        binding.socialLoginBlock.vkBt.setOnClickListener {
            findNavController().navigate(R.id.navigation_main)
        }
        binding.socialLoginBlock.yandexBt.setOnClickListener {
            // TODO: Yandex login logic
        }
    }



    private fun setupInputListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentTil =
                    if (binding.emailEt.hasFocus()) binding.emailTil else if (binding.passwordEt.hasFocus()) binding.passwordTil else null
                currentTil?.let {
                    if (it.isErrorEnabled) {
                        it.error = null
                        it.isErrorEnabled = false
                        it.boxBackgroundColor = ContextCompat.getColor(
                            requireContext(),
                            mad.training.uikit.R.color.input_bg
                        )
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                updateButtonState()
                if (!binding.emailTil.hasFocus()) {
                    updateTextInputLayoutAppearance(
                        binding.emailTil,
                        binding.emailEt.text.isNullOrEmpty(),
                        false
                    )
                }
                if (!binding.passwordTil.hasFocus()) {
                    updateTextInputLayoutAppearance(
                        binding.passwordTil,
                        binding.passwordEt.text.isNullOrEmpty(),
                        false
                    )
                }
            }
        }

        binding.emailEt.addTextChangedListener(textWatcher)
        binding.passwordEt.addTextChangedListener(textWatcher)

        val focusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            val til = when (view.id) {
                R.id.emailEt -> binding.emailTil
                R.id.passwordEt -> binding.passwordTil
                else -> null
            }
            til?.let {
                if (!it.isErrorEnabled) { // Обновляем вид только если нет активной ошибки
                    updateTextInputLayoutAppearance(
                        it,
                        (it.editText?.text.isNullOrEmpty()),
                        hasFocus
                    )
                }
            }
        }

        binding.emailEt.onFocusChangeListener = focusChangeListener
        binding.passwordEt.onFocusChangeListener = focusChangeListener
    }

    private fun updateButtonState() {
        val emailIsNotEmpty = binding.emailEt.text?.isNotEmpty() ?: false
        val passwordIsNotEmpty = binding.passwordEt.text?.isNotEmpty() ?: false
        binding.nextBt.isEnabled = emailIsNotEmpty && passwordIsNotEmpty
    }

    /**
     * Обновляет внешний вид TextInputLayout (цвет обводки) в зависимости от его состояния.
     * Фон при ошибке управляется отдельно при установке/снятии ошибки.
     */
    private fun updateTextInputLayoutAppearance(
        textInputLayout: TextInputLayout,
        isEmpty: Boolean,
        hasFocus: Boolean
    ) {
        if (textInputLayout.isErrorEnabled && !hasFocus) {
            return
        }
        if (hasFocus) {
            textInputLayout.boxStrokeColor = ContextCompat.getColor(
                requireContext(),
                mad.training.uikit.R.color.uikit_keypad_button_background_default
            ) // Используем R из uikit!
            return
        }

        if (isEmpty) {
            textInputLayout.boxStrokeColor =
                ContextCompat.getColor(requireContext(), mad.training.uikit.R.color.input_stroke)
        } else {
            textInputLayout.boxStrokeColor =
                ContextCompat.getColor(requireContext(), mad.training.uikit.R.color.input_icon)
        }
    }
}