package org.lifetowncolumbus.pos.services

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Transaction
import org.lifetowncolumbus.pos.merchant.models.Account
import org.lifetowncolumbus.pos.merchant.models.AccountTransactionResult
import org.lifetowncolumbus.pos.merchant.models.AccountTransactionResult.FAILURE
import org.lifetowncolumbus.pos.merchant.models.AccountTransactionResult.SUCCESS


class BankService {
    private val MAXIMUM_BALANCE = 12.0
    private val db = FirebaseFirestore.getInstance()

    fun chargeCard(accountNumber: String, amt: Double, callback: (AccountTransactionResult) -> Unit) {
        val docRef = db.collection("accounts").document(accountNumber)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            if(snapshot.exists()) {
                val account = Account.from(snapshot)

                if(account.expired()) {
                    account.reset()
                }

                account.balance = account.balance + amt
                updateAccount(account, transaction, docRef, callback)
            }
            else {
                val account = Account(accountNumber, amt)
                updateAccount(account, transaction, docRef, callback)
            }

        }.addOnSuccessListener { result ->
            callback(SUCCESS)
            Log.d(TAG, "Transaction success: ${result!!}")
        }.addOnFailureListener { e ->
            Log.e(TAG, "Transaction failure.", e)
            callback(FAILURE)
        }

    }

    private fun updateAccount(
        account: Account,
        transaction: Transaction,
        docRef: DocumentReference,
        callback: (AccountTransactionResult) -> Unit
    ): Any {
        return if (account.balance <= MAXIMUM_BALANCE) {
            transaction.set(docRef, account, SetOptions.mergeFields(Account.FIELDS))
        } else {
            callback(FAILURE)
        }
    }
}