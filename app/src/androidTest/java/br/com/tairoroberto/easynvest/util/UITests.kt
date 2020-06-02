package br.com.tairoroberto.easynvest.util

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.snackbar.Snackbar
import org.hamcrest.Matchers.allOf

object UITests {

    fun checkHintIsDisplayed(text: Any) {
        if (text is String) {
            onView(withHint(text)).check(matches(isDisplayed()))
        } else if (text is Int) {
            onView(withHint(text)).check(matches(isDisplayed()))
        }
    }

    fun inputTextWithReplaceById(id: Int, inputText: String) {
        onView(withId(id)).perform(click(), replaceText(inputText), closeSoftKeyboard())
    }

    // Click button
    fun clickById(id: Int) {
        onView(withId(id)).perform(click())
    }

    //Instrumentation elements multiple hierarchy
    fun isTextDisplayed(text: String): Boolean {
        return try {
            onView(allOf(withText(text))).check(matches(isDisplayed()))
            true
        } catch (e: Exception) {
            false
        }
    }

    fun isTextDisplayed(id: Int, text: String): Boolean {
        return try {
            onView(allOf(withId(id), withText(text))).check(matches(isDisplayed()))
            true
        } catch (e: Exception) {
            false
        }
    }

    fun checkSnackbar(text: String) {
        withText(text).matches(
            allOf(
                isDescendantOfA(isAssignableFrom(Snackbar.SnackbarLayout::class.java)),
                isCompletelyDisplayed()
            )
        )
    }
}
