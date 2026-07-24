package gg.happy.bingo.module.game.impl

import gg.happy.bingo.module.conf.Conf
import gg.happy.bingo.module.game.GamePhase
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.unregisterListener

object Waiting : GamePhase
{
    var playerJoinListener: ProxyListener? = null
    var playerQuitListener: ProxyListener? = null

    override fun onStart()
    {
        playerJoinListener = registerBukkitListener(PlayerJoinEvent::class.java)
        {
            it.player.teleport(Conf.spawn)
        }
    }

    override fun onEnd()
    {
        playerJoinListener?.let { unregisterListener(it) }
    }
}