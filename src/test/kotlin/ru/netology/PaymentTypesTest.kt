package ru.netology

import org.junit.Test

import org.junit.Assert.*
import java.lang.Exception

class PaymentTypesTest {

    @Test
    fun calCommission() {
        val num = PaymentTypes.MASTERCARD.number
        val type = PaymentTypes.getByNum(num)
        val sum = 15000L * 100;
        val curMonthSum = 0L
        val result = type?.calCommission(sum = sum, curMonthSum = curMonthSum)
        assertEquals(0L, result)
    }

    @Test
    fun calCommission__shouldNotAddCommission() {
        val num = PaymentTypes.VK_PAY.number
        val type = PaymentTypes.getByNum(num)
        val sum = 15000L * 100;
        val curMonthSum = 0L
        val result = type?.calCommission(sum = sum, curMonthSum = curMonthSum)
        assertEquals(0L, result)
    }

    @Test
    fun calCommission_shouldAddCommission() {
        val num = PaymentTypes.VISA.number
        val type = PaymentTypes.getByNum(num)
        val sum = 10000L * 100;
        val curMonthSum = 0L
        val result = type?.calCommission(sum = sum, curMonthSum = curMonthSum)
        assertEquals(7500L, result)
    }

    @Test
    fun calCommission_monthLimitExceed() {
        assertThrows(MonthLimitException::class.java) {
            val num = PaymentTypes.MASTERCARD.number
            val type = PaymentTypes.getByNum(num)
            val sum = 75000L * 100;
            val curMonthSum = 60000000L
            type?.calCommission(sum = sum, curMonthSum = curMonthSum)
        }
    }

    @Test
    fun calCommission_oneTimeLimitExceed() {
        assertThrows(OneTimeLimitException::class.java) {
            val num = PaymentTypes.VK_PAY.number
            val type = PaymentTypes.getByNum(num)
            val sum = 15000001L;
            val curMonthSum = 0L
            type?.calCommission(sum = sum, curMonthSum = curMonthSum)
        }
    }
}