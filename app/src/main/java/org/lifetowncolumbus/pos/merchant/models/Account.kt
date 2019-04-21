package org.lifetowncolumbus.pos.merchant.models

import com.google.firebase.firestore.DocumentSnapshot
import org.threeten.bp.LocalDate


data class Account (
    var id: String = "",
    var balance: Double = 0.0,
    var dayCreated: Long = LocalDate.now().toEpochDay()
){
    fun reset() {
        balance = 0.0
        dayCreated = LocalDate.now().toEpochDay()
    }

    val isExpired: Boolean
        get() {
            return LocalDate.now().isAfter(LocalDate.ofEpochDay(dayCreated))
        }

    companion object {
        fun from(snapshot: DocumentSnapshot) : Account {
            return snapshot.toObject(Account::class.java)!!
        }
    }
}
