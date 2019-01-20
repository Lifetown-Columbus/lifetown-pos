package org.lifetowncolumbus.pos.merchant

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.lifetowncolumbus.pos.R

class CatalogInstrumentedTest : TestHarness() {

    @get:Rule
    var activityRule: ActivityTestRule<MerchantActivity> = ActivityTestRule(MerchantActivity::class.java)

    @Test
    fun editCatalog_addNewCatalogItem() {
        onView(withId(R.id.editCatalogButton)).perform(click())
        onView(withId(R.id.addCatalogItemButton)).perform(click())
        onView(withId(R.id.catalogItemName)).perform(typeText("Flerp"))
        onView(withId(R.id.catalogItemValue)).perform(typeText("5.5"))

        onView(withId(R.id.saveCatalogItemButton)).perform(click())
        onView(withId(R.id.addSaleItem)).check(matches(withText("Flerp")))
    }
}