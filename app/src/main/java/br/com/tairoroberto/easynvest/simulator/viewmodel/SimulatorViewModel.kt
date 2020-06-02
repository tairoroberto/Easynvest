package br.com.tairoroberto.easynvest.simulator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tairoroberto.easynvest.core.State
import br.com.tairoroberto.easynvest.simulator.model.SimulatorResponse
import br.com.tairoroberto.easynvest.simulator.repository.SimulatorRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SimulatorViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: SimulatorRepository
) : ViewModel() {
    private val _response = MutableLiveData<State<SimulatorResponse>>()
    val response: LiveData<State<SimulatorResponse>>
        get() = _response

    fun simulate(
        investedAmount: Double?,
        rate: Long?,
        maturityDate: String?
    ) {
        viewModelScope.launch {
            try {
                _response.value = State.loading(true)
                val response = withContext(ioDispatcher) {
                    repository.simulate(
                        investedAmount, "CDI", rate, false, maturityDate
                    )
                }

                _response.value = State.success(response)

            } catch (throwable: Throwable) {
                _response.value = State.error(throwable)
            }
        }
    }
}