package com.example.jettipapp.util

fun calculateTotalTip(totalBill: Double?, tipPercentage: Int) =
    if (totalBill != null) (totalBill * tipPercentage) / 100 else 0.0

fun calculateTotalPerPerson(totalBill: Double?, splitBy: Int, tipPercentage: Int): Double {
    val bill = calculateTotalTip(totalBill, tipPercentage) + (totalBill ?: 0.0)
    return bill / splitBy
}