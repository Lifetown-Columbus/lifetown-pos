package org.lifetowncolumbus.pos.merchant


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.lifetowncolumbus.pos.R

@LargeTest
class CheckoutInstrumentedTest : TestHarness() {

    @get:Rule
    var activityRule: ActivityTestRule<MerchantActivity> = ActivityTestRule(MerchantActivity::class.java)

    @Test
    fun testAddItem_computeTotal() {
        addAnItem()

        onView(withId(R.id.total)).check(matches(withText("Total: $500.00")))
    }

    @Test
    fun addItem_payWithCash_itemizedListContainsPurchasesAndPayment() {
        addAnItem()
        payCashExpectingChange()

        onView(withId(R.id.total)).check(matches(withText("Change Due: $100.00")))
    }

    private fun payCashExpectingChange() {
        onView(withId(R.id.payCashButton)).perform(click())
        onView(withId(R.id.amountTendered))
            .perform(typeText("600"), closeSoftKeyboard())

        onView(withId(R.id.acceptCashButton)).perform(click())
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("Cash Payment"))))
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("-$600.00"))))
    }

    private fun addAnItem() {
        onView(withId(R.id.itemValue))
            .perform(typeText("500"), closeSoftKeyboard())

        onView(withId(R.id.addItemButton)).perform(click())
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("Purchased Item"))))
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("$500.00"))))
    }

    @Test
    fun payWithCash_navigatesToSaleComplete() {
        addAnItem()
        payCashExpectingChange()

        onView(withId(R.id.newSaleButton)).check(matches(isDisplayed()))
    }

    @Test
    fun payWithCash_clickCancel_navigatesBack() {
        onView(withId(R.id.payCashButton)).perform(click())
        onView(withId(R.id.cancelPaymentButton)).perform(click())
        onView(withId(R.id.addItemButton)).check(matches(isDisplayed()))
    }

    @Test
    fun payWithCash_acceptButtonDisabled_whenInsufficientAmountGiven() {
        addAnItem()

        onView(withId(R.id.payCashButton)).perform(click())
        onView(withId(R.id.amountTendered))
            .perform(typeText("400"), closeSoftKeyboard())

        onView(withId(R.id.acceptCashButton)).check(matches(not(isEnabled())))
    }

    @Test
    fun saleComplete_clickNewSale_NavigatesToCheckout() {
        addAnItem()
        payCashExpectingChange()

        onView(withId(R.id.newSaleButton)).perform(click())
        onView(withId(R.id.total)).check(matches(withText("Total: $0.00")))
    }
}
