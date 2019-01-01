package org.lifetowncolumbus.pos.checkout

import java.math.BigDecimal

interface Item {
    val value: BigDecimal
    val name: String
}

data class PurchasedItem internal constructor(override val value: BigDecimal, override val name: String = "Purchased Item") : Item {
    companion object {
        fun worth(value: Double): PurchasedItem {
            return PurchasedItem(BigDecimal.valueOf(value))
        }
    }
}
data class CashPayment internal constructor(override val value: BigDecimal, override val name: String = "Cash Payment") : Item {

    companion object {
        fun worth(value: Double): CashPayment {
            return CashPayment(BigDecimal.valueOf(-value))
        }
    }
}
