package gg.happy.bingo.module.listener

import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.SubscribeEvent

object PlayerDeathListener
{
    @SubscribeEvent
    fun onPlayerDeath(event: PlayerDeathEvent)
    {
        event
    }
}