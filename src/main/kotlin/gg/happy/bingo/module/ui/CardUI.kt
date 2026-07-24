package gg.happy.bingo.module.ui

import gg.happy.bingo.module.Card
import gg.happy.bingo.module.PlayerData
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import taboolib.common.platform.service.PlatformExecutor
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.getI18nName
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Chest
import taboolib.platform.util.asLangText
import taboolib.platform.util.asLangTextList
import taboolib.platform.util.buildItem
import taboolib.platform.util.sendLang
import taboolib.platform.util.submit

val closeNameAni = listOf(
    "&cC&7lose",
    "&cCl&7ose",
    "&cClo&7se",
    "&cClos&7e",
    "&cClose",
    "&7C&close",
    "&7Cl&cose",
    "&7Clo&cse",
    "&7Clos&ce",
    "&7Close",
)

fun Player.openCard(showReward: Boolean = false) =
    openMenu<Chest>(asLangText("card-title")) {
        rows(5)
        map(
            "#       x",
            "#       #",
            "#       #",
            "#       #",
            "#       $",
        )
        set('#', XMaterial.GRAY_STAINED_GLASS_PANE) { name = " " }

        set('x', XMaterial.RED_STAINED_GLASS_PANE)
        var task: PlatformExecutor.PlatformTask? = null
        onBuild { _, inventory ->
            task = submit(period = 4) {
                inventory.getItem(8)
            }
        }
        onClose {
            task?.cancel()
        }

        val lineReward = Card.getLineReward()
        set('$', XMaterial.NAME_TAG) {
            name = asLangText("show-reward", lineReward)
            lore += asLangTextList("show-reward-lore", lineReward)
        }

        val team = PlayerData.get(this@openCard)?.team

        for (i in 0 until 5)
        {
            for (j in 0 until 5)
            {
                val slot = i * 9 + j + 2
                val index = i * 5 + j
                if (team?.completed?.getOrNull(index) ?: false)
                {
                    val reward = Card.getReward(index)
                    set(slot, buildItem(Card.items[index].material) {
                        lore += asLangTextList("card-item-lore", reward)
                        amount = if (showReward) reward else 1
                    }) {
                        val event = clickEvent()
                        val node = when (event.click)
                        {
                            ClickType.LEFT -> "mark"
                            ClickType.RIGHT -> "mark-mine"
                            ClickType.SHIFT_LEFT -> "mark-yours"
                            ClickType.MIDDLE -> "mark-ignore"
                            else -> return@set
                        }
                        team.players.forEach {
                            it.sendLang(
                                node,
                                Card.items[index].material.getI18nName(this@openCard)
                            )
                        }
                    }
                } else
                {
                    val bingoTimes = team?.slotsBingoTimes?.getOrNull(index) ?: 0
                    set(
                        slot, buildItem(
                            when (bingoTimes)
                            {
                                0 -> Material.LIME_STAINED_GLASS_PANE
                                1 -> Material.YELLOW_STAINED_GLASS_PANE
                                else -> Material.ORANGE_STAINED_GLASS_PANE
                            }
                        ) {
                            name = asLangText(
                                "completed", when (bingoTimes)
                                {
                                    0 -> "&a"
                                    1 -> "&e"
                                    else -> "&6"
                                }
                            )
                        })
                }
            }
        }
    }