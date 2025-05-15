package mad.training.tmatule.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import mad.training.network.util.ErrorType
import mad.training.tmatule.databinding.FragmentHomeBinding
import mad.training.tmatule.presentation.base.BaseFragment
import mad.training.tmatule.presentation.home.adapters.DepartmentsAdapter
import mad.training.uikit.PrimaryButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()
    private val departmentAdapter: DepartmentsAdapter by lazy { DepartmentsAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchDepartments()
        updateUi()
        setTextListener()
        observeAdapter()
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

    override fun observeViewModel() {
        super.observeViewModel()
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.visibility = View.GONE
            binding.departmentRv.visibility = View.GONE
            binding.layoutNoInternetContainer.visibility = View.GONE

            when (state) {
                is DepartmentsUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is DepartmentsUiState.Success -> {
                    binding.departmentRv.visibility = View.VISIBLE
                    departmentAdapter.submitList(state.data)
                }

                is DepartmentsUiState.ShowError -> {
                    // ВСЕГДА показываем диалог, согласно заданию
                    showErrorDialog(state.message)
                    // Если это ошибка "нет интернета", ТОЛЬКО ТОГДА показываем спец. экран
                    if (state.type == ErrorType.NO_INTERNET) {
                        binding.layoutNoInternetContainer.visibility = View.VISIBLE
                        binding.departmentRv.visibility =
                            View.GONE // Скрываем список, если он был виден
                    } else {
                        // Для других ошибок сервера можно оставить RV видимым (например, если там были старые данные)
                        // или скрыть, если он пуст. Решай по макету.
                        // binding.departmentRv.visibility = View.VISIBLE // (или GONE)
                    }
                }
            }
        }
    }

    private fun observeAdapter() {
        binding.departmentRv.apply {
            adapter = departmentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showErrorDialog(message: String) {
        if (!isAdded || requireActivity().isFinishing) return
        AlertDialog.Builder(requireContext())
            .setTitle("Ошибка")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false) // Закрывается только пользователем
            .show()
    }


}