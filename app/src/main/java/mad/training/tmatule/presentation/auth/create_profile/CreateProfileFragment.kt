package mad.training.tmatule.presentation.auth.create_profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import mad.training.tmatule.databinding.FragmentCreateProfileBinding
import mad.training.tmatule.presentation.base.BaseFragment
import mad.training.uikit.InputFieldView // Убедись, что импорт InputFieldView есть
import mad.training.uikit.PrimaryButton // Убедись, что импорт PrimaryButton есть
// import mad.training.uikit.SelectorFieldView // Если он в том же пакете uikit

class CreateProfileFragment :
    BaseFragment<FragmentCreateProfileBinding>(FragmentCreateProfileBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUiChanges()
        setupFieldListeners()
        checkFieldsAndSetButtonState() // Начальная проверка состояния кнопки
    }

    private fun setUiChanges() {
        binding.nameEt.setHint("Имя")
        binding.surnameEt.setHint("Отчество") // Это поле Отчество? В XML было lastNameEt
        binding.lastNameEt.setHint("Фамилия")
        binding.dateBirthdayEt.setHint("Дата рождения") // Возможно, понадобится DatePicker
        binding.telegramEt.setHint("Telegram")

        binding.genderSelector.apply {
            setHint("Пол")
            setItems(listOf("Не выбрано", "Женский", "Мужской")) { selected ->
                // checkFieldsAndSetButtonState() // Вызываем проверку при выборе из селектора
                // Toast.makeText(context, "Вы выбрали: $selected", Toast.LENGTH_SHORT).show()
                // Важно: нужно обеспечить, чтобы setItems также вызывал checkFieldsAndSetButtonState
                // или чтобы SelectorFieldView имел свой OnSelectionChangedListener
            }
        }
    }

    private fun setupFieldListeners() {
        // Создаем TextWatcher, который будем использовать для всех InputFieldView
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checkFieldsAndSetButtonState()
            }
        }

        binding.nameEt.setOnTextChanged { checkFieldsAndSetButtonState() }
        binding.lastNameEt.setOnTextChanged { checkFieldsAndSetButtonState() }
        binding.surnameEt.setOnTextChanged { checkFieldsAndSetButtonState() }
        binding.dateBirthdayEt.setOnTextChanged { checkFieldsAndSetButtonState() }
        binding.telegramEt.setOnTextChanged { checkFieldsAndSetButtonState() }


        binding.genderSelector.setOnSelectionChangedListener {
            checkFieldsAndSetButtonState()
        }

        binding.createBt.setOnClickListener {
            if (binding.createBt.currentState == PrimaryButton.State.ACTIVE) {
                val name = binding.nameEt.getText()
                val lastName = binding.lastNameEt.getText()
                val surname = binding.surnameEt.getText()
                // ... и так далее для всех полей
                Toast.makeText(requireContext(), "Профиль создается...", Toast.LENGTH_SHORT).show()
                // TODO: Логика создания профиля
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkFieldsAndSetButtonState() {
        val isNameValid = binding.nameEt.getText().isNotBlank()
        val isLastNameValid = binding.lastNameEt.getText().isNotBlank()
        val isSurnameValid = binding.surnameEt.getText().isNotBlank() // Отчество
        val isDateBirthdayValid = binding.dateBirthdayEt.getText().isNotBlank()
        val isTelegramValid = binding.telegramEt.getText().isNotBlank()
        val selectedGender = binding.genderSelector.getSelectedValue()
        val isGenderSelected = selectedGender != null && selectedGender != "Не выбрано" // Убедись, что "Не выбрано" - это правильная строка


        val allFieldsValid = isNameValid &&
                isLastNameValid &&
                isSurnameValid &&
                isDateBirthdayValid &&
                isTelegramValid &&
                isGenderSelected

        if (allFieldsValid) {
            binding.createBt.setState(PrimaryButton.State.ACTIVE)
        } else {
            binding.createBt.setState(PrimaryButton.State.DISABLED)
        }
    }
}