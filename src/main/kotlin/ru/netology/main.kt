package ru.netology

import java.text.NumberFormat
import kotlin.math.min

val rubWordForm = arrayOf("рубль", "рубля", "рублей")
val pennyWordForm = arrayOf("копейка", "копейки", "копеек")

fun main() {
    val mapMonthSum: MutableMap<PaymentTypes, Long> = PaymentTypes.values().associateTo(mutableMapOf()) { it to 0 }
    while (true) {
        try {
            println("Выберите тип оплаты (exit для выхода):")
            printAllPayments()
            val lineType = readLine()
            if (lineType == "exit") {
                break;
            }
            val typeNum = lineType!!.toInt()
            val type = PaymentTypes.getByNum(typeNum) ?: throw NotFoundProductType()
            println("Введите сумму перевода:")
            val lineSum = readLine()
            val sum = lineSum!!.toLong() * 100
            if (sum < 0) {
                throw NegativeNumberException()
            }
            val commission = type.calCommission(sum, mapMonthSum[type]!!)
            mapMonthSum[type] = sum + mapMonthSum[type]!!;
            println("Коммиссия составляет ${getSumString(commission)}")
        } catch (e: MonthLimitException) {
            println("Превышен месячный лимит")
        } catch (e: NegativeNumberException) {
            println("Значение должно быть положительным числом")
        } catch (e: NotFoundProductType) {
            println("Введён неверный тип продукта")
        } catch (e: NumberFormatException) {
            println("Введено некоректное значение")
        } catch (e: OneTimeLimitException) {
            println("Для текущей оплаты превышен разовый лимит платежа")
        }
    }
}

fun printAllPayments() {
    enumValues<PaymentTypes>().forEach { it.print() }
}

fun getPluralForm(count: Long, words: Array<String>): String {
    val cases = intArrayOf(2, 0, 1, 1, 1, 2)

    return words[if (count % 100 in 5..19) 2 else cases[min(count % 10, 5).toInt()]]
}

fun getSumString(sum: Long): String {
    val rub = sum / 100
    val penny = sum % 100

    val rubWord = getPluralForm(rub, rubWordForm)
    val pennyWord = getPluralForm(penny, pennyWordForm)

    val builder = StringBuilder()
    builder.append("${NumberFormat.getNumberInstance().format(rub)} $rubWord")
    if (penny > 0) {
        builder.append(" ")
        if (rub > 0) {
            builder.append(penny.toString().padStart(2, '0'))
        } else {
            builder.append(penny)
        }
        builder.append(" $pennyWord")
    }

    return builder.toString()
}