package gg.happy.bingo.module

import gg.happy.bingo.module.conf.Conf
import gg.happy.bingo.util.getOrdinalSuffix
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import taboolib.module.nms.getI18nName
import taboolib.platform.util.asLangText
import taboolib.platform.util.hasItem
import taboolib.platform.util.sendLang
import kotlin.math.min

class Team(val name: String)
{
    companion object
    {
        val teams = mutableListOf<Team>()
    }

    var point = 0

    val players = mutableListOf<Player>()

    val offlinePlayers = mutableListOf<OfflinePlayer>()

    val completed = MutableList(Card.SIZE) { false }

    val completedLines = MutableList(Card.LINES.size) { false }

    val slotsBingoTimes = MutableList(Card.SIZE) { 0 }

    fun check()
    {
        checkItem()
        checkLine()
    }

    private fun checkItem()
    {
        for (i in 0 until Card.SIZE)
        {
            if (completed[i])
                continue
            Card.items[i].let { item ->
                players.firstOrNull { it.inventory.hasItem { it.type == item.material } }?.let { player ->
                    val rank = item.completed + 1
                    val reward = Card.getReward(i)
                    completed[i] = true
                    point += reward
                    item.completed++

                    Bukkit.getOnlinePlayers().forEach {
                        it.sendLang(
                            if (it in players) "complete" else "complete-enemy",
                            it.asLangText(name),
                            player.name,
                            item.material.getI18nName(it),
                            rank,
                            rank.getOrdinalSuffix(),
                            reward
                        )
                    }
                }
            }
        }
    }

    fun checkLine()
    {
        for (i in Card.LINES.indices)
        {
            if (completedLines[i])
                continue
            if (Card.LINES[i].indexes.firstOrNull { !completed[i] } == null)
            {
                val rank = Card.lineCompleted + 1
                val reward = Card.getLineReward()
                completedLines[i] = true
                point += reward
                Card.lineCompleted++

                Bukkit.getOnlinePlayers().forEach {
                    it.sendLang(
                        if (it in players) "complete-line" else "complete-line-enemy",
                        it.asLangText(name),
                        it.asLangText(Card.LINES[i].name),
                        rank,
                        rank.getOrdinalSuffix(),
                        reward
                    )
                }
            }
        }
    }
}