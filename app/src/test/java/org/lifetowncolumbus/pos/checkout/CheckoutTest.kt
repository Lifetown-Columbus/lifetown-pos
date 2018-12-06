package org.lifetowncolumbus.pos.checkout

import android.widget.Button
import android.widget.TextView
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.lifetowncolumbus.pos.R
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@Ignore
class CheckoutTest {

    val subject : CheckoutActivity by lazy {
        Robolectric.setupActivity(CheckoutActivity::class.java)
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
