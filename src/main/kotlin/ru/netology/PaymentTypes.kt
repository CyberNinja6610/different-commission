package ru.netology

import kotlin.math.max
import kotlin.math.roundToLong

enum class PaymentTypes(var title: String, var number: Int, var monthLimit: Long) {
    MASTERCARD("Mastercard", 1, 60000000),
    MAESTRO("Maestro", 2, 60000000),
    VISA("Visa", 3, 60000000),
    MIR("Мир", 4, 60000000),
    VK_PAY("VK Pay", 5, 4000000);

    private var oneTimeLimit = 15000000

    fun print() = println("${this.number}) ${this.title}")
    fun calCommission(sum: Long, curMonthSum: Long): Long {
        if (this == VK_PAY && sum > oneTimeLimit) {
            throw OneTimeLimitException()
        }
        if ((curMonthSum + sum) > this.monthLimit) {
            throw MonthLimitException()
        }
        return when (this) {
            MAESTRO, MASTERCARD -> if (sum > 7500000) (sum * 0.006).roundToLong() + 20 else 0
            VISA, MIR -> max((sum * 0.00075).roundToLong(), 3500)
            VK_PAY -> 0
        }
    }

    companion object {
        fun getByNum(num: Int): PaymentTypes? = values().find { it.number == num }
    }
}