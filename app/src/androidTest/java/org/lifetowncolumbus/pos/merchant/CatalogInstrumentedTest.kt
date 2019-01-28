package org.lifetowncolumbus.pos.merchant

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.models.CatalogItem

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

    @Test
    fun editCatalog_editCatalogItem() {
        catalogItemDao.insert(CatalogItem(null, "Widget", 500.00))
        onView(withId(R.id.editCatalogButton)).perform(click())

        onView(withId(R.id.editCatalogRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.catalogItemName)).perform(typeText("NewWidget"))
        onView(withId(R.id.catalogItemValue)).perform(typeText("99"))

        onView(withId(R.id.saveCatalogItemButton)).perform(click())
        onView(withId(R.id.addSaleItem)).check(matches(withText("NewWidget")))


    }
}