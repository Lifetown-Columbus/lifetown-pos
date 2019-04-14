package org.lifetowncolumbus.pos.services

import com.google.firebase.functions.FirebaseFunctions

class BankService {
    private val functions = FirebaseFunctions.getInstance()

    fun test() {
        functions.getHttpsCallable("helloWorld").call()
    }

}