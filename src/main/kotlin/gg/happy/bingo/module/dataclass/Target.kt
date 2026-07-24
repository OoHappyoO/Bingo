package gg.happy.bingo.module.dataclass

import org.bukkit.Material

data class Target(var material: Material)
{
    var completed: Int = 0
}