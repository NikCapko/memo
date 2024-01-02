package com.nikcapko.memo.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.nikcapko.memo.R
import com.nikcapko.memo.ui.BaseTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@MediumTest
internal class MainActivityTest : BaseTest() {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    override fun setUp() {
        super.setUp()
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun checkContainerIsDisplayed() {
        onView(ViewMatchers.withId(R.id.fcView)).check(matches(isDisplayed()))
    }
}
