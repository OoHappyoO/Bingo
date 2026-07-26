package gg.happy.bingo.util

fun tickToFormatted(tick: Int): String
{
    val s = (tick + 19) / 20
    val m = s / 60
    val h = m / 60
    return when
    {
        h > 0 -> "${h}h ${m % 60}m ${s % 60}s"
        m > 0 -> "${m}m ${s % 60}s"
        else -> "${s}s"
    }
}