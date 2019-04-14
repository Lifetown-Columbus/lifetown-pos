package org.lifetowncolumbus.pos.services

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


class BankService {
    val STARTING_BALANCE = 12.0
    val db = FirebaseFirestore.getInstance()

    fun withdraw(account: String, amt: Double, onSuccess: () -> Unit, onDeclined: () -> Unit) {
        val docRef = db.collection("accounts").document(account)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            if(snapshot.exists()) {
                //TODO read this before the transaction because you must write something if you start a transaction
                // maybe we don't need a transaction
                val currentBalance : Double = snapshot.getDouble("balance") ?: 0.0
                val newBalance = currentBalance - amt

                if (newBalance > 0) {
                    transaction.update(docRef, "balance", (snapshot.getDouble("balance")!! - amt))
                } else {
                    onDeclined()
                }
            }
            else {
                val newBalance = STARTING_BALANCE - amt

                if (newBalance > 0 ) {
                    val data = HashMap<String, Any>()
                    data["balance"] = newBalance
                    transaction.set(docRef, data)
                } else {
                    onDeclined()
                }
            }
        }.addOnSuccessListener { result ->
            onSuccess()
            Log.d(TAG, "Transaction success: ${result!!}")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Transaction failure.", e)
            onDeclined()
        }


    }


}