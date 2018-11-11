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

    val subject : Checkout by lazy {
        Robolectric.setupActivity(Checkout::class.java)
    }

    @Test
    fun startingTheActivity_shouldSetTheSumToZero() {
        assertEquals(total(), "$0.00")
    }

    @Test
    fun addingItem_shouldSumTheValue() {
        addItem("2")
        addItem("2")

        assertEquals(total(), "$4.00")
    }

    @Test
    fun enteringAnInvalidValue_shouldNotChangeTheSum() {
        addItem("twenty dollars")

        assertEquals(total(), "$0.00")
    }

    private fun total() = subject.findViewById<TextView>(R.id.total).text

    private fun addItem(value: String) {
        subject.findViewById<TextView>(R.id.itemValue).text = value
        subject.findViewById<Button>(R.id.addItemButton).performClick()
    }
}
