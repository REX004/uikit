package mad.training.tmatule.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import mad.training.tmatule.databinding.FragmentHomeBinding
import mad.training.tmatule.presentation.base.BaseFragment
import mad.training.uikit.PrimaryButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
        setTextListener()
    }

    private fun updateUi() {
        binding.inputExample.apply {
            setHint("FuckNigga")
        }
    }

    private var type = 1

    private fun setTextListener() {
        binding.inputExample.setOnTextChanged {
            if (binding.inputExample.getText() == "sex") {
                binding.inputExample.setError("No sex")
                binding.btnAdd.setState(PrimaryButton.State.ACTIVE)
            } else {
                binding.inputExample.setError(null)
            }
        }
        binding.selectorField.apply {
            setHint("Выберите страну")
            setItems(listOf("Россия", "Казахстан", "Беларусь")) { selected ->
                Toast.makeText(context, "Вы выбрали: $selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchField.setOnTextChangedListener { query ->

        }

        binding.btnAdd.setState(PrimaryButton.State.OUTLINED)
        binding.btnAdd.setOnClickListener {
            if (type != 2) {
                binding.btnAdd.setState(PrimaryButton.State.DISABLED)
            } else {
                binding.btnAdd.setState(PrimaryButton.State.ACTIVE)
            }
            type++
        }


    }


}