package gg.happy.bingo.module

import org.bukkit.Bukkit

object ScoreboardManager
{
    val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard!!
}