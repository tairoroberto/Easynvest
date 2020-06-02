package br.com.tairoroberto.easynvest.sumulator.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SimulatorResponse(
    @SerializedName("dailyGrossRateProfit")
    val dailyGrossRateProfit: Double = 0.0,
    @SerializedName("investmentParameter")
    val investmentParameter: InvestmentParameter,
    @SerializedName("monthlyGrossRateProfit")
    val monthlyGrossRateProfit: Double = 0.0,
    @SerializedName("netAmount")
    val netAmount: Double = 0.0,
    @SerializedName("grossAmountProfit")
    val grossAmountProfit: Double = 0.0,
    @SerializedName("annualGrossRateProfit")
    val annualGrossRateProfit: Double = 0.0,
    @SerializedName("taxesAmount")
    val taxesAmount: Double = 0.0,
    @SerializedName("netAmountProfit")
    val netAmountProfit: Double = 0.0,
    @SerializedName("rateProfit")
    val rateProfit: Double = 0.0,
    @SerializedName("grossAmount")
    val grossAmount: Double = 0.0,
    @SerializedName("taxesRate")
    val taxesRate: Double = 0.0,
    @SerializedName("annualNetRateProfit")
    val annualNetRateProfit: Double = 0.0
) : Parcelable