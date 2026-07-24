package gg.happy.bingo.util

fun Int.getOrdinal() = this.toString() + getOrdinalSuffix()

fun Int.getOrdinalSuffix() = when
{
    this in 11..13 -> "th"
    this % 10 == 1 -> "st"
    this % 10 == 2 -> "nd"
    this % 10 == 3 -> "rd"
    else -> "th"
}