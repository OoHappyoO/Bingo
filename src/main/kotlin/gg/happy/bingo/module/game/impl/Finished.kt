package gg.happy.bingo.module.game.impl

import gg.happy.bingo.module.game.GamePhase
import gg.happy.bingo.module.listener.SneakSwapListener
import org.bukkit.Bukkit
import taboolib.platform.util.sendLang

object Finished: GamePhase
{
    override fun onStart()
    {
        Bukkit.getOnlinePlayers().forEach {
            it.sendLang("finished")
        }
    }

    override fun onEnd()
    {
        SneakSwapListener.unregister()
    }
}