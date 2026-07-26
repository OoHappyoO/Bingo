package gg.happy.bingo.module

import gg.happy.bingo.module.conf.Conf
import gg.happy.bingo.module.game.impl.Main
import gg.happy.bingo.util.tickToFormatted
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object Placeholder : PlaceholderExpansion
{
    override val identifier: String
        get() = Conf.placeholderIdentifier

    override fun onPlaceholderRequest(player: Player?, args: String): String =
        when
        {
            args.startsWith("card_material_") -> Card.items.getOrNull(
                args.substringAfter("card_material_").toInt()
            )?.material.toString()

            args.startsWith("card_completed_") ->
                player?.let { PlayerData.get(it) }?.team?.let {
                    it.completed[args.substringAfter("card_completed_").toInt()].toString()
                } ?: "null"

            args == "time" -> tickToFormatted(Main.timer)

            args == "point" -> player?.let { PlayerData.get(it)?.team?.point.toString() } ?: "null"

            else -> "null"
        }
}