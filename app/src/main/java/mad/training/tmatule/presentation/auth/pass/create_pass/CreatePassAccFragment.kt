package mad.training.tmatule.presentation.auth.pass.create_pass

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import mad.training.tmatule.R
import mad.training.tmatule.databinding.FragmentCreatePassAccBinding
import mad.training.tmatule.presentation.base.BaseFragment
import mad.training.uikit.PinKeyboardView

class CreatePassAccFragment :
    BaseFragment<FragmentCreatePassAccBinding>(FragmentCreatePassAccBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.pinPad.setOnPinButtonClickListener(object :
            PinKeyboardView.OnPinButtonClickListener {
            override fun onDigitClicked(digit: Char) {
                binding.staus.appendDigit(digit)
                Log.d("CreatePassAccFragment", "Текущий PIN: ${binding.staus.getPin()}")
            }

            override fun onBackspaceClicked() {
                binding.staus.deleteDigit() // Вызываем метод у PinCodeFieldView
                Log.d(
                    "CreatePassAccFragment",
                    "Текущий PIN после удаления: ${binding.staus.getPin()}"
                )
            }
        })

        binding.staus.onPinFilledListener = { pin ->
            handlePinCompleted(pin)
        }

        binding.missBt.setOnClickListener {
            Toast.makeText(requireContext(), "Действие 'Пропустить' нажато", Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.navigation_main) // Замени на свой ID
        }
    }

    private fun handlePinCompleted(pin: String) {
        // PIN введен полностью
        Log.i("CreatePassAccFragment", "PIN полностью введен: $pin. Длина: ${pin.length}")
        Toast.makeText(requireContext(), "PIN успешно создан: $pin", Toast.LENGTH_LONG).show()

        // Здесь должна быть твоя логика:
        // - Сохранение PIN-кода (например, в ViewModel, SharedPreferences (зашифрованно!), или передача дальше)
        // - Переход на следующий экран
        // - Показ сообщения об успехе/ошибке и т.д.

        // Пример: очистить поле и клавиатуру для следующего возможного ввода
        // или если это экран подтверждения, то не очищать.
        // binding.staus.clear() // Очищаем поле отображения

        // Пример навигации на следующий экран (например, подтверждение PIN или главный экран)
        // findNavController().navigate(R.id.action_createPassAccFragment_to_confirmPassFragment, Bundle().apply {
        //     putString("created_pin", pin)
        // })
        // ИЛИ
        // findNavController().navigate(R.id.action_createPassAccFragment_to_homeFragment)
        // Замени R.id.action... на актуальные ID из твоего navigation graph.
    }

    override fun onDestroyView() {
        // Важно очистить слушатели, чтобы избежать утечек памяти
        binding.pinPad.setOnPinButtonClickListener(null)
        binding.staus.onPinFilledListener = null
        super.onDestroyView()
    }
}