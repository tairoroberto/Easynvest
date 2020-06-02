package br.com.tairoroberto.easynvest.simulator.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.tairoroberto.easynvest.R
import br.com.tairoroberto.easynvest.core.extensions.formatValue
import br.com.tairoroberto.easynvest.core.extensions.formatValueCurrency
import br.com.tairoroberto.easynvest.core.extensions.parseDate
import br.com.tairoroberto.easynvest.simulator.model.SimulatorResponse
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment() {

    var simulatorResponse: SimulatorResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        simulatorResponse = arguments?.getParcelable("RESPONSE")
        fillResponse(simulatorResponse)

        buttonSimulateAgain.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun fillResponse(simulatorResponse: SimulatorResponse?) {
        textview_result.formatValueCurrency(simulatorResponse?.grossAmount)
        textInvestedAmount.formatValueCurrency(simulatorResponse?.investmentParameter?.investedAmount)
        textGrossAmount.formatValueCurrency(simulatorResponse?.grossAmount)
        textGrossAmountProfitLabel.formatValueCurrency(simulatorResponse?.grossAmountProfit)
        textGrossAmountProfit.formatValueCurrency(simulatorResponse?.grossAmountProfit)
        textTaxesAmount.formatValueCurrency(simulatorResponse?.taxesAmount)
        textTaxesAmount.text =
            "${textTaxesAmount.text.toString()} [${simulatorResponse?.taxesRate}%]"
        textNetAmount.formatValueCurrency(simulatorResponse?.netAmount)
        textMaturityDate.parseDate(simulatorResponse?.investmentParameter?.maturityDate.toString())
        textMaturityTotalDays.text =
            simulatorResponse?.investmentParameter?.maturityTotalDays.toString()
        textMonthlyGrossRateProfit.formatValue(simulatorResponse?.monthlyGrossRateProfit)
        textRate.text = "${simulatorResponse?.investmentParameter?.rate.toString()}%"
        textAnnualGrossRateProfit.formatValue(simulatorResponse?.annualGrossRateProfit)
        textRateProfit.formatValue(simulatorResponse?.rateProfit)
    }
}
