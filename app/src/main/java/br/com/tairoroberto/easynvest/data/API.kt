package br.com.tairoroberto.easynvest.data

import br.com.tairoroberto.easynvest.sumulator.model.SimulatorResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    companion object {
        const val SIMULATE = "calculator/simulate"
    }

    @GET(SIMULATE)
    suspend fun simulate(
        @Query("investedAmount") investedAmount: Double?,
        @Query("index") index: String?,
        @Query("rate") rate: Long?,
        @Query("isTaxFree") isTaxFree: Boolean?,
        @Query("maturityDate") maturityDate: String?
    ): SimulatorResponse
}