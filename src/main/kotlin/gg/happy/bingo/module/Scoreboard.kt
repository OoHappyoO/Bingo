package gg.happy.bingo.module

import gg.happy.bingo.Bingo
import me.neznamy.tab.api.TabAPI
import me.neznamy.tab.api.event.player.PlayerLoadEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.chat.colored

object Scoreboard
{
    val conf = Bingo.scoreboardConf

    private val scoreboardManager = TabAPI.getInstance().scoreboardManager!!

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

    @Awake(LifeCycle.ENABLE)
    fun init()
    {
        TabAPI.getInstance().eventBus!!.register(PlayerLoadEvent::class.java) {
            scoreboardManager.showScoreboard(it.player, waiting)
        }
    }
}