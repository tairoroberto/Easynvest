package br.com.tairoroberto.easynvest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.tairoroberto.easynvest.core.State
import br.com.tairoroberto.easynvest.simulator.model.SimulatorResponse
import br.com.tairoroberto.easynvest.simulator.repository.SimulatorRepository
import br.com.tairoroberto.easynvest.simulator.viewmodel.SimulatorViewModel
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.exceptions.base.MockitoException
import java.io.IOException

@ExperimentalCoroutinesApi
class SimulatorViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private var repository = Mockito.mock(SimulatorRepository::class.java)

    @Before
    fun up() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        testCoroutineDispatcher.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

    @Test
    fun testCalculateLoadingState() = testCoroutineDispatcher.runBlockingTest {
        val viewModel = SimulatorViewModel(testCoroutineDispatcher, repository)
        testCoroutineDispatcher.pauseDispatcher()

        doReturn(getSimulatorResponse()).`when`(repository)
            .simulate(200.0, "CDI", 100, false, "2023-06-03")

        viewModel.simulate(200.0, 100, "2023-06-03")
        assertThat(viewModel.response.value).isEqualTo(State.loading<SimulatorResponse>(true))
    }

    @Test
    fun testCalculateLoadingAndSuccessState() = testCoroutineDispatcher.runBlockingTest {
        val viewModel = SimulatorViewModel(testCoroutineDispatcher, repository)
        testCoroutineDispatcher.pauseDispatcher()

        doReturn(getSimulatorResponse()).`when`(repository)
            .simulate(200.0, "CDI", 100, false, "2023-06-03")

        viewModel.simulate(200.0, 100, "2023-06-03")
        assertThat(viewModel.response.value).isEqualTo(State.loading<SimulatorResponse>(true))

        testCoroutineDispatcher.resumeDispatcher()
        assertThat(viewModel.response.value).isEqualTo(State.success(getSimulatorResponse()))
    }

    @Test(expected = MockitoException::class)
    fun testCalculateDateErrorState() = testCoroutineDispatcher.runBlockingTest {
        val viewModel = SimulatorViewModel(testCoroutineDispatcher, repository)
        testCoroutineDispatcher.pauseDispatcher()

        doThrow(IOException::class.java).`when`(repository)
            .simulate(200.0, "CDI", 100, false, "2020-06-01")

        viewModel.simulate(200.0, 100, "2020-06-01")
        assertThat(viewModel.response.value).isEqualTo(State.loading<SimulatorResponse>(true))

        testCoroutineDispatcher.resumeDispatcher()
        assertThat(viewModel.response.value).isEqualTo(State.error<SimulatorResponse>(IOException()))
    }

    private fun getSimulatorResponse(): SimulatorResponse? {
        return Gson().fromJson(
            "{\"investmentParameter\":{\"investedAmount\":200.0,\"yearlyInterestRate\":4.8731,\"maturityTotalDays\":1096,\"maturityBusinessDays\":777,\"maturityDate\":\"2023-06-03T00:00:00\",\"rate\":100.0,\"isTaxFree\":false},\"grossAmount\":231.60,\"taxesAmount\":4.74,\"netAmount\":226.86,\"grossAmountProfit\":31.60,\"netAmountProfit\":26.86,\"annualGrossRateProfit\":15.80,\"monthlyGrossRateProfit\":0.4,\"dailyGrossRateProfit\":0.000188830770053494,\"taxesRate\":15.0,\"rateProfit\":4.8731,\"annualNetRateProfit\":13.43}",
            SimulatorResponse::class.java
        )
    }
}