package br.com.tairoroberto.easynvest.simulator.repository

import br.com.tairoroberto.easynvest.data.ApiService
import br.com.tairoroberto.easynvest.simulator.model.SimulatorResponse

class SimulatorRepository {
    suspend fun simulate(
        investedAmount: Double?,
        index: String?,
        rate: Long?,
        isTaxFree: Boolean?,
        maturityDate: String?
    ): SimulatorResponse {
        return ApiService.getApi().simulate(
            investedAmount,
            index,
            rate,
            isTaxFree,
            maturityDate
        )
    }
}