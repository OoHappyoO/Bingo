package gg.happy.bingo.module.command

import gg.happy.bingo.module.ItemsGUI
import gg.happy.bingo.module.conf.Conf
import gg.happy.bingo.module.game.GameManager
import gg.happy.bingo.module.game.impl.Ready
import gg.happy.bingo.module.game.impl.Waiting
import gg.happy.bingo.util.runKether
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.int
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang

@CommandHeader("Bingo")
object Command
{
    @CommandBody(permissionDefault = PermissionDefault.TRUE)
    val main = mainCommand {
        execute<Player> { sender, _, _ ->
            if (GameManager.phase == Waiting)
            {
                sender.sendLang("cant-use-main")
                return@execute
            }
            sender.runKether(Conf.mainCommand)
        }
    }

    @CommandBody
    val start = subCommand {
        execute<CommandSender> { sender, _, _ ->
            if (GameManager.phase == Waiting)
                GameManager.phase = Ready
            else
                sender.sendLang("cant-use-start")
        }
    }

    @CommandBody
    val items = subCommand {
        literal("edit") {
            int("page", optional = true) {
                execute<Player> { sender, context, _ ->
                    ItemsGUI.init()
                    ItemsGUI.getUI(context.int("page")).openFor(sender)
                }
            }
            execute<Player> { sender, _, _ ->
                ItemsGUI.init()
                ItemsGUI.getUI(1).openFor(sender)
            }
        }
        literal("save") {
            execute<Player> { sender, _, _ ->
                ItemsGUI.save()
                sender.sendLang("items-saved")
            }
        }
    }

    @CommandBody
    val debug = subCommand {
        execute<Player> { sender, _, _ ->
        }
    }
}