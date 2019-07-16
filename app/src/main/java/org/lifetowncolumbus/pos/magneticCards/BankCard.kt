package org.lifetowncolumbus.pos.magneticCards

class BankCard(value: String) {
    private final val matcher = Regex(";([0-9]{1,19})(.*)\\?")

    val isValid = matcher.matches(value)
    var accountNumber = ""

    init {
        if(isValid) {
            val matchResult = matcher.find(value)
            if (matchResult != null) {
                accountNumber = matchResult.groups[1]?.value ?: ""
            }

        }
    }

}