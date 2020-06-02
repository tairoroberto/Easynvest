package br.com.tairoroberto.easynvest.simulator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.tairoroberto.easynvest.simulator.repository.SimulatorRepository
import kotlinx.coroutines.CoroutineDispatcher

class SimulatorViewModelFactory(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: SimulatorRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            CoroutineDispatcher::class.java,
            SimulatorRepository::class.java
        ).newInstance(ioDispatcher, repository)
    }
}