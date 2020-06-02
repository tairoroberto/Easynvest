package br.com.tairoroberto.easynvest.simulator.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.tairoroberto.easynvest.R
import br.com.tairoroberto.easynvest.core.AppUtil.hideKeyboard
import br.com.tairoroberto.easynvest.core.Status
import br.com.tairoroberto.easynvest.core.extensions.*
import br.com.tairoroberto.easynvest.simulator.repository.SimulatorRepository
import br.com.tairoroberto.easynvest.simulator.viewmodel.SimulatorViewModel
import br.com.tairoroberto.easynvest.simulator.viewmodel.SimulatorViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sumulator.*
import kotlinx.coroutines.Dispatchers

class SimulatorFragment : Fragment() {

    private val viewModel: SimulatorViewModel by lazy {
        val factory = SimulatorViewModelFactory(Dispatchers.IO, SimulatorRepository())
        ViewModelProvider(this, factory).get(SimulatorViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sumulator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editInvestedAmount.moneyMasK()
        editMaturityDate.setCurrentDate()
        editMaturityDate.dateMask()

        buttonSimulate.setOnClickListener {
            if (validate())
                viewModel.simulate(
                    investedAmount = editInvestedAmount.unMaskMoney(),
                    rate = editRate.unMaskMoney().toLong(),
                    maturityDate = editMaturityDate.getDateFormatted()
                )
            hideKeyboard(view)
        }

        observeResponse()
    }

    private fun validate(): Boolean {

        if (editInvestedAmount.unMaskMoney() <= 0) {
            val snackbar: Snackbar = Snackbar.make(buttonSimulate, "", Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(Color.RED)
            snackbar.setText("Valor não deve ser vazio")
            snackbar.setAction("OK", null)
            snackbar.show()
            editInvestedAmount.requestFocus()
            return false
        }

        return true
    }

    private fun observeResponse() {
        viewModel.response.observe(viewLifecycleOwner, Observer {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) {
                return@Observer
            }
            progress.visibility = if (it.loading == true) VISIBLE else GONE
            when (it.status) {
                Status.ERROR -> Snackbar.make(
                    buttonSimulate,
                    "Falha ao simular",
                    Snackbar.LENGTH_SHORT
                ).show()
                Status.SUCCESS -> {
                    findNavController().navigate(
                        R.id.action_SimulatorFragment_to_ResultFragment,
                        Bundle().apply {
                            putParcelable("RESPONSE", it.data)
                        })
                }
                Status.LOADING -> {
                }
            }
        })
    }
}
