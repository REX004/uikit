package mad.training.tmatule.presentation.home

import mad.training.tmatule.databinding.FragmentHomeBinding
import mad.training.tmatule.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()
    override fun applyClick() {
        super.applyClick()

    }
}