package br.com.tairoroberto.easynvest.simulator

import androidx.test.rule.ActivityTestRule
import br.com.tairoroberto.easynvest.data.ApiService
import br.com.tairoroberto.easynvest.robots.SimulatorRobot
import br.com.tairoroberto.easynvest.sumulator.view.MainActivity
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SimulatorTest {
    private lateinit var robot: SimulatorRobot
    private var mockWebServer: MockWebServer? = null

    init {
        mockWebServer = MockWebServer()
        ApiService.setBaseUrl(mockWebServer?.url("/").toString())
    }

    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun setUp() {
        robot = SimulatorRobot(mockWebServer, activityRule)
    }

    @After
    fun finishActivity() {
        mockWebServer?.shutdown();
        robot.finishActivity()
    }

    @Test
    fun testFieldsAreShowingOnScreen() {
        robot.startActivity()
        robot.checkFieldsIsShowing()
    }

    @Test
    fun testErrorOnSendEmptyValue() {
        robot.startActivity()
        robot.checkErrorEmptyValue()
    }

    @Test
    fun testSuccessOnSendCorrectValue() {
        robot.setRequest()
        robot.startActivity()
        robot.checkCalculate()
    }

    @Test
    fun testSuccessOnSendCorrectValueAndBack() {
        robot.setRequest()
        robot.startActivity()
        robot.checkCalculateAndBack()
    }

    @Test
    fun testRunAllCheckRobots() {
        robot.setRequest()
        robot.startActivity()
        robot.checkFieldsIsShowing()
            .checkErrorEmptyValue()
            .checkCalculate()
    }
}