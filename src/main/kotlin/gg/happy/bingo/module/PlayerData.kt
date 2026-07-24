package gg.happy.bingo.module

import org.bukkit.entity.Player
import java.util.UUID

class PlayerData(
    val player: Player,
    val team: Team,
)
{
    companion object
    {
        val data = hashMapOf<UUID, PlayerData>()

        fun get(uuid: UUID): PlayerData? = data[uuid]

        fun get(player: Player): PlayerData? = data[player.uniqueId]
    }
}