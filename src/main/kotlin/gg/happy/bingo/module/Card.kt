package gg.happy.bingo.module

import gg.happy.bingo.module.conf.Conf
import gg.happy.bingo.module.dataclass.Line
import gg.happy.bingo.module.dataclass.Target
import taboolib.common.util.random

object Card
{
    const val SIZE = 25

    val LINES = listOf(
        Line("row-1", listOf(0, 1, 2, 3, 4)),
        Line("row-2", listOf(5, 6, 7, 8, 9)),
        Line("row-3", listOf(10, 11, 12, 13, 14)),
        Line("row-4", listOf(15, 16, 17, 18, 19)),
        Line("row-5", listOf(20, 21, 22, 23, 24)),
        Line("col-1", listOf(0, 5, 10, 15, 20)),
        Line("col-2", listOf(1, 6, 11, 16, 21)),
        Line("col-3", listOf(2, 7, 12, 17, 22)),
        Line("col-4", listOf(3, 8, 13, 18, 23)),
        Line("col-5", listOf(4, 9, 14, 19, 24)),
        Line("diagonal", listOf(0, 6, 12, 18, 24)),
        Line("anti-diagonal", listOf(4, 8, 12, 16, 20))
    )

    var toSelect = Conf.items.toMutableList()

    val items = MutableList(SIZE) { Target(toSelect.removeAt(random(toSelect.size))) }

    var lineCompleted = 0

    fun generate()
    {
        toSelect = Conf.items.toMutableList()
        for (i in 0 until SIZE)
            items[i] = Target(toSelect.removeAt(random(toSelect.size)))
    }
}