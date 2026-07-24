package gg.happy.bingo.module

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class Team
{
    val point = 0

    val players = mutableListOf<Player>()

    val offlinePlayers = mutableListOf<OfflinePlayer>()
}