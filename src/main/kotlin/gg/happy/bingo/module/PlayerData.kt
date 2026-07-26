package gg.happy.bingo.module

import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import java.util.UUID

class PlayerData(
    val player: Player,
    var team: Team?,
)
{
    companion object
    {
        val data = hashMapOf<UUID, PlayerData>()

        fun get(uuid: UUID): PlayerData? = data[uuid]

        fun get(player: Player): PlayerData? = data[player.uniqueId]

        @SubscribeEvent
        fun onPlayerJoin(event: PlayerJoinEvent)
        {
            if (!data.containsKey(event.player.uniqueId))
                data[event.player.uniqueId] = PlayerData(event.player, null)
        }

        fun isPlayer(player: Player): Boolean = get(player.uniqueId)?.team != null
    }
}