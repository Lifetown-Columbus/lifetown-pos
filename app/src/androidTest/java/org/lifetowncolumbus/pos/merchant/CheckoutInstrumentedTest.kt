package org.lifetowncolumbus.pos.merchant


import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.models.CatalogItem

@LargeTest
class CheckoutInstrumentedTest : TestHarness() {

    @get:Rule
    var activityRule: ActivityTestRule<POSActivity> = ActivityTestRule(POSActivity::class.java)

    @Before
    fun setup() {
        catalogItemDao.insert(CatalogItem(null, "Widget", 500.00))
        Thread.sleep(500)
    }

    @Test
    fun payButtons_areDisabled_WhenNoItems_Added() {
        onView(withId(R.id.payCashButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.payCreditButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.quickCashButton)).check(matches(not(isEnabled())))
    }

    @Test
    fun payQuickCash_GoesDirectlyTo_SaleComplete() {
        addAnItem()
        onView(withId(R.id.quickCashButton)).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("Cash Payment"))))
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("-$500.00"))))
        onView(withId(R.id.printReceiptButton)).check(matches(isDisplayed()))
    }

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
        onView(withId(R.id.payCashButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.amountTendered))
            .perform(typeText("600"), closeSoftKeyboard())

        onView(withId(R.id.acceptCashButton)).perform(click())
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("Cash Payment"))))
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("-$600.00"))))
    }


    private fun addAnItem() {
        onView(withId(R.id.catalogRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(500)
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("Widget"))))
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("$500.00"))))
    }

    @Test
    fun payWithDebitCard_navigatesToSwipeCard() {
        addAnItem()

        onView(withId(R.id.payCreditButton)).perform(click())
        onView(withId(R.id.payCreditButton)).check(matches(not(isEnabled())))

        onView(withId(R.id.swipeCardMessage)).check(matches(withText("Please Swipe Card")))
    }

    @Test
    fun removeItem_removesTheItem() {
        addAnItem()
        onView(withId(R.id.removeItemButton)).perform(click())
        onView(withId(R.id.itemized_list)).check(matches(hasChildCount(0)))
    }

    @Test
    fun payWithCash_navigatesToSaleComplete() {
        addAnItem()

        payCashExpectingChange()

        onView(withId(R.id.printReceiptButton)).check(matches(isDisplayed()))
    }

    @Test
    fun payWithDebitCard_clickCancel_navigatesBack() {
        addAnItem()
        onView(withId(R.id.payCreditButton)).perform(click())
        onView(withId(R.id.cancelSwipeButton)).perform(click())
        onView(withId(R.id.addSaleItem)).check(matches(isDisplayed()))
    }


    @Test
    fun payWithCash_clickCancel_navigatesBack() {
        addAnItem()
        onView(withId(R.id.payCashButton)).perform(click())
        onView(withId(R.id.cancelPaymentButton)).perform(click())
        onView(withId(R.id.addSaleItem)).check(matches(isDisplayed()))
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
    fun saleComplete_clickDoNotPrint_NavigatesToCheckout() {
        addAnItem()
        payCashExpectingChange()

        onView(withId(R.id.dontPrintReceiptButton)).perform(click())
        onView(withId(R.id.total)).check(matches(withText("Total: $0.00")))
    }

    @Test
    fun saleComplete_clickPrint_NavigatesToCheckout() {
        addAnItem()
        payCashExpectingChange()

        onView(withId(R.id.printReceiptButton)).perform(click())
        onView(withId(R.id.total)).check(matches(withText("Total: $0.00")))
    }
}
