package br.com.tairoroberto.easynvest.robots

import android.content.Context
import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import br.com.tairoroberto.easynvest.R
import br.com.tairoroberto.easynvest.data.API
import br.com.tairoroberto.easynvest.sumulator.view.MainActivity
import br.com.tairoroberto.easynvest.util.UITests
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException
import java.io.InputStream

class SimulatorRobot(
    private val mockWebServer: MockWebServer?,
    private val activityTestRule: ActivityTestRule<MainActivity>
) {

    fun startActivity() {
        activityTestRule.launchActivity(Intent(Intent.ACTION_MAIN))
    }

    fun finishActivity() {
        activityTestRule.finishActivity()
    }

    fun setRequest(): SimulatorRobot {
        mockWebServer?.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path?.contains(API.SIMULATE) == true) {
                    return MockResponse().setBody(
                        readFileFromAssets(
                            "response.json",
                            InstrumentationRegistry.getInstrumentation().context
                        )
                    )
                }

                return MockResponse().setBody(
                    readFileFromAssets(
                        "error_not_found.json",
                        InstrumentationRegistry.getInstrumentation().context
                    )
                )
            }
        }
        return this
    }

    fun checkFieldsIsShowing(): SimulatorRobot {
        UITests.checkHintIsDisplayed("R$")
        UITests.checkHintIsDisplayed("dia/mês/ano")
        UITests.checkHintIsDisplayed("100%")
        return this
    }

    fun checkCalculate(): SimulatorRobot {
        UITests.inputTextWithReplaceById(R.id.editInvestedAmount, "R$ 200,00")
        UITests.inputTextWithReplaceById(R.id.editMaturityDate, "03/06/2023")
        UITests.inputTextWithReplaceById(R.id.editRate, "100")
        UITests.clickById(R.id.buttonSimulate)
        UITests.isTextDisplayed("R$ 31,60")

        return this
    }

    fun checkErrorEmptyValue(): SimulatorRobot {
        UITests.inputTextWithReplaceById(R.id.editMaturityDate, "01/06/2023")
        UITests.inputTextWithReplaceById(R.id.editRate, "100")
        UITests.clickById(R.id.buttonSimulate)
        UITests.checkSnackbar("Valor não deve ser vazio")

        return this
    }

    fun checkCalculateAndBack(): SimulatorRobot {
        UITests.inputTextWithReplaceById(R.id.editInvestedAmount, "R$ 200,00")
        UITests.inputTextWithReplaceById(R.id.editMaturityDate, "03/06/2023")
        UITests.inputTextWithReplaceById(R.id.editRate, "100")
        UITests.clickById(R.id.buttonSimulate)
        UITests.isTextDisplayed("R$ 31,60")
        UITests.clickById(R.id.buttonSimulateAgain)
        UITests.isTextDisplayed("Quanto você gostaria de aplicar? *")
        return this
    }

    private fun readFileFromAssets(fileName: String, c: Context): String {
        return try {
            val `is`: InputStream = c.assets.open(fileName)
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}