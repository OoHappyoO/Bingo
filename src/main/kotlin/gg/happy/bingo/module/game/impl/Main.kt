package gg.happy.bingo.module.game.impl

import gg.happy.bingo.module.Scoreboard
import gg.happy.bingo.module.Team
import gg.happy.bingo.module.game.GameManager
import gg.happy.bingo.module.game.GamePhase
import org.bukkit.Bukkit
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.platform.util.sendLang

object Main : GamePhase
{

    var onTick: PlatformExecutor.PlatformTask? = null

    var timer = 54000

    override fun onStart()
    {
        Bukkit.getOnlinePlayers().forEach {
            it.sendLang("started")
            Scoreboard.showing = Scoreboard.main
        }
        onTick = submit(period = 1) {
            Team.onTick()

            timer--
            if (timer <= 0)
                GameManager.phase = Finished
        }
    }

    override fun onEnd()
    {
        onTick?.cancel()
    }
}