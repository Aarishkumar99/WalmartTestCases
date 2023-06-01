package com.example.walmart.presentation.details
import android.os.Bundle
import android.view.View
import kotlin.reflect.KClass
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.walmart.domain.di.ServiceProvider.get
import com.example.walmart.presentation.R
import com.example.walmart.presentation.databinding.CountryDetailsFragmentBinding
import com.example.walmart.presentation.ext.repeatOnStart
import kotlinx.coroutines.flow.collectLatest

class CountryDetailsFragment : Fragment(R.layout.country_details_fragment) {

    var viewModel: CountryDetailsViewModel? = null

    private var viewBinding: CountryDetailsFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = CountryDetailsFragmentBinding.bind(view)
        val viewModelFactory = get<CountryDetailsViewModelFactory>()
        viewModel = ViewModelProvider(this, viewModelFactory).get(CountryDetailsViewModel::class.java)
        repeatOnStart { viewModel!!.state.collectLatest(::renderState) }
        repeatOnStart { viewModel!!.effectFlow.collectLatest(::onEffect) }
        viewBinding?.apply {
            swipeRefreshLayout.setOnRefreshListener { viewModel!!.reloadList() }
            actionBar.setNavigationOnClickListener { viewModel!!.onBackClicked() }
        }
    }

    override fun onDestroyView() {
        viewBinding = null
        super.onDestroyView()
    }

    fun renderState(state: CountryDetailsViewModel.State) {
        viewBinding?.apply {
            swipeRefreshLayout.isRefreshing = state.loading
            errorMessage.text = state.errorMessage
            state.country?.run {
                with(countryItemLayout) {
                    nameWithRegionView.text =
                        root.context.getString(R.string.format_name_with_region, name, region)
                    codeView.text = code
                    capitalView.text = capital
                }
            }
        }
    }

    fun onEffect(state: CountryDetailsViewModel.Effect) {
        when (state) {
            CountryDetailsViewModel.Effect.OnBack -> findNavController().popBackStack()
        }
    }
}