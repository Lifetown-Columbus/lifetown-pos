package org.lifetowncolumbus.pos.checkout


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.lifetowncolumbus.pos.R

@RunWith(AndroidJUnit4::class)
@LargeTest
class CheckoutInstrumentedTest {

    @get:Rule
    var activityRule: ActivityTestRule<MerchantActivity> = ActivityTestRule(MerchantActivity::class.java)

    @Test
    fun testAddItem_computeTotal() {
        onView(withId(R.id.itemValue))
            .perform(typeText("5"), closeSoftKeyboard())

        onView(withId(R.id.addItemButton)).perform(click())

        onView(withId(R.id.total)).check(matches(withText("Total: $5.00")))
    }

    @Test
    fun addItem_payWithCash() {
        onView(withId(R.id.itemValue))
            .perform(typeText("500"), closeSoftKeyboard())

        onView(withId(R.id.addItemButton)).perform(click())
        onView(withId(R.id.payCashButton)).perform(click())

        onView(withId(R.id.amountTendered))
            .perform(typeText("600"), closeSoftKeyboard())

        onView(withId(R.id.calculateChangeButton)).perform(click())

        onView(withId(R.id.total)).check(matches(withText("Change Due: $100.00")))
    }
}
