package org.lifetowncolumbus.pos.checkout

import java.math.BigDecimal

interface Item {
    val value: BigDecimal
}

data class PurchasedItem(override val value: BigDecimal) : Item {
    companion object {
        fun worth(value: Double): PurchasedItem {
            return PurchasedItem(BigDecimal.valueOf(value))
        }
    }
}
data class CashPayment(override val value: BigDecimal) : Item {

    companion object {
        fun worth(value: Double): CashPayment {
            return CashPayment(BigDecimal.valueOf(-value))
        }
    }
}
