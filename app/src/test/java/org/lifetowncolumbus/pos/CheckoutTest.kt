package org.lifetowncolumbus.pos

import android.widget.Button
import android.widget.TextView
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.lifetowncolumbus.pos.activities.Checkout
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CheckoutTest {
    @Test
    fun addingItem_shouldSumTheValue() {
        val activity : Checkout = Robolectric.setupActivity(Checkout::class.java)

        activity.findViewById<TextView>(R.id.itemValue).text = "2"
        activity.findViewById<Button>(R.id.addItemButton).performClick()

        activity.findViewById<TextView>(R.id.itemValue).text = "2"
        activity.findViewById<Button>(R.id.addItemButton).performClick()

        val result = activity.findViewById<TextView>(R.id.total).text

        assertEquals(result, "$4.00")
    }
}
