package org.lifetowncolumbus.pos.services

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Transaction
import org.lifetowncolumbus.pos.merchant.models.Account
import org.lifetowncolumbus.pos.merchant.models.AccountTransactionResult
import org.lifetowncolumbus.pos.merchant.models.AccountTransactionResult.ERROR
import org.lifetowncolumbus.pos.merchant.models.AccountTransactionResult.DECLINED
import org.lifetowncolumbus.pos.merchant.models.AccountTransactionResult.SUCCESS


class BankService {
    private val MAXIMUM_BALANCE = 12.0
    private val db = FirebaseFirestore.getInstance()

    fun chargeCard(accountNumber: String, amt: Double, callback: (AccountTransactionResult) -> Unit) {
        val docRef = db.collection("accounts").document(accountNumber)
        var result: AccountTransactionResult? = null

        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            if(snapshot.exists()) {
                val account = Account.from(snapshot)

                if(account.expired()) {
                    account.reset()
                }

                account.balance = account.balance + amt
                result = updateAccount(account, transaction, docRef)
            }
            else {
                val account = Account(accountNumber, amt)
                result = updateAccount(account, transaction, docRef)
            }

        }.addOnSuccessListener { _ ->
            Log.e(TAG, "Transaction success")
            callback(result!!)
        }.addOnFailureListener { e ->
            Log.e(TAG, "Transaction failure.", e)
            callback(ERROR)
        }

    }

    private fun updateAccount(
        account: Account,
        transaction: Transaction,
        docRef: DocumentReference
    ): AccountTransactionResult {
        return if (account.balance <= MAXIMUM_BALANCE) {
            transaction.set(docRef, account, SetOptions.mergeFields(Account.FIELDS))
            SUCCESS
        } else {
            Log.e(TAG, "Card Declined")
            DECLINED
        }
    }
}