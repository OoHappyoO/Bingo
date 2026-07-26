package gg.happy.bingo.module

import gg.happy.bingo.Bingo
import me.neznamy.tab.api.TabAPI
import me.neznamy.tab.api.TabPlayer
import me.neznamy.tab.api.event.player.PlayerLoadEvent
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.chat.colored

object Scoreboard
{
    val conf = Bingo.scoreboardConf

    val api = TabAPI.getInstance()

    val scoreboardManager = api.scoreboardManager!!

    val waiting = scoreboardManager.createScoreboard(
        "waiting",
        conf.getString("waiting.title", " ")!!.colored(),
        conf.getStringList("waiting.lines")
    )

    val main = scoreboardManager.createScoreboard(
        "main",
        conf.getString("main.title", " ")!!.colored(),
        conf.getStringList("main.lines")
    )

    var showing = waiting
        set(value)
        {
            Bukkit.getOnlinePlayers().forEach {
                api.getPlayer(it.uniqueId)?.let { tabPlayer -> scoreboardManager.showScoreboard(tabPlayer, value) }
                field = value
            }
        }

    @Awake(LifeCycle.ENABLE)
    fun init()
    {
        TabAPI.getInstance().eventBus!!.register(PlayerLoadEvent::class.java) {
            scoreboardManager.showScoreboard(it.player, showing)
        }
    }
}