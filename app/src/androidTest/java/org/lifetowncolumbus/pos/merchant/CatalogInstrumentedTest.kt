package org.lifetowncolumbus.pos.merchant

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.lifetowncolumbus.pos.PrinterWrapper
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.models.CatalogItem


class CatalogInstrumentedTest : TestHarness() {

    @get:Rule
    var activityRule: ActivityTestRule<POSActivity> = ActivityTestRule(POSActivity::class.java)

    @Test
    fun editCatalog_addNewCatalogItem() {
        navigate_to_catalog_config()
        onView(withId(R.id.addCatalogItemButton)).perform(click())
        onView(withId(R.id.catalogItemName)).perform(typeText("Flerp"))
        onView(withId(R.id.catalogItemValue)).perform(typeText("5.5"))

        save_and_close_config()
        onView(withId(R.id.addSaleItem)).check(matches(withText("Flerp")))
        onView(withId(R.id.addSaleItem)).perform(click())
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("Flerp"))))
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("$5.50"))))
    }

    private fun navigate_to_catalog_config() {
        openActionBarOverflowOrOptionsMenu(context)
        onView(withText("Settings")).perform(click())
        onView(withId(R.id.editCatalogButton)).perform(click())
    }

    private fun save_and_close_config() {
        onView(withId(R.id.saveCatalogItemButton)).perform(click())
        onView(isRoot()).perform(pressBack())
        onView(withId(R.id.closeConfigButton)).perform(click())
    }

    @Test
    fun navigateToCheckout_whenConfigIsClosed() {
        navigate_to_catalog_config()
        onView(isRoot()).perform(pressBack())
        onView(withId(R.id.closeConfigButton)).perform(click())
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
            .check(matches(withText("Current Sale")))
    }


    @Test
    fun editCatalog_editCatalogItem() {
        catalogItemDao.insert(CatalogItem(null, "Widget", 500.00))
        navigate_to_catalog_config()

        onView(withId(R.id.editCatalogRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(R.id.catalogItemName)).perform(clearText(), typeText("NewWidget"))
        onView(withId(R.id.catalogItemValue)).perform(clearText(), typeText("99"))

        save_and_close_config()
        onView(withId(R.id.addSaleItem)).check(matches(withText("NewWidget")))
        onView(withId(R.id.addSaleItem)).perform(click())
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("NewWidget"))))
        onView(withId(R.id.itemized_list)).check(matches(hasDescendant(withText("$99.00"))))
    }

    @Test
    fun editCatalog_clickDelete_shouldDeleteIt() {
        catalogItemDao.insert(CatalogItem(null, "Widget", 500.00))
        catalogItemDao.insert(CatalogItem(null, "Flerbert", 500.00))
        navigate_to_catalog_config()

        onView(withId(R.id.editCatalogRecyclerView)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText("WIDGET")), click()))

        onView(withId(R.id.deleteCatalogItemButton)).perform(click())
        onView(withId(R.id.addSaleItem)).check(matches(not(withText("Widget"))))
        onView(withId(R.id.addSaleItem)).check(matches(withText("Flerbert")))
    }
}