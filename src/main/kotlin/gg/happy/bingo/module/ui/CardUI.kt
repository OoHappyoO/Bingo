package gg.happy.bingo.module.ui

import gg.happy.bingo.module.Card
import gg.happy.bingo.module.PlayerData
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.platform.service.PlatformExecutor
import taboolib.library.xseries.XMaterial
import taboolib.module.chat.colored
import taboolib.module.nms.getI18nName
import taboolib.module.ui.lockSlots
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Chest
import taboolib.platform.util.asLangText
import taboolib.platform.util.asLangTextList
import taboolib.platform.util.buildItem
import taboolib.platform.util.modifyLore
import taboolib.platform.util.modifyMeta
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

fun Player.openCard()
{
    openMenu<Chest>(asLangText("card-title")) {
        val team = PlayerData.get(this@openCard)?.team
        rows(5)
        map(
            "#       x",
            "#       #",
            "#       #",
            "#       #",
            "#       $",
        )
        val lockedSlots = (0 until 45).toList()
        onClick(lock = false) { event ->
            event.lockSlots(lockedSlots)
        }

        set('#', XMaterial.GRAY_STAINED_GLASS_PANE) { name = " " }

        set('x', XMaterial.RED_STAINED_GLASS_PANE)
        onClick('x') {
            with(it.clicker) {
                playSound(this, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f)
                closeInventory()
            }
        }
        var task: PlatformExecutor.PlatformTask? = null
        onBuild { _, inventory ->
            var i = 0
            task = submit(period = 4) {
                inventory.getItem(8)?.modifyMeta<ItemMeta> {
                    setDisplayName(closeNameAni[i++].colored())
                    i %= closeNameAni.size
                }
            }
        }
        onClose {
            task?.cancel()
        }

        val lineReward = Card.getLineReward()
        var showRewards = false

        set('$', XMaterial.NAME_TAG) {
            name = asLangText("show-reward", lineReward)
            lore += asLangTextList("show-reward-lore", lineReward)
        }
        onClick('$') {
            it.clicker.playSound(it.clicker, Sound.UI_BUTTON_CLICK, 0.5f, 1.0f)
            showRewards = !showRewards
            it.inventory.setItem(44, it.inventory.getItem(44)?.let { itemStack ->
                buildItem(itemStack) {
                    if (showRewards)
                        shiny()
                }
            })
            forEachItemSlot { slot, index ->
                if (!(team?.completed?.getOrNull(index) ?: false))
                {
                    it.inventory.setItem(slot, it.inventory.getItem(slot)?.let { itemStack ->
                        buildItem(itemStack) {
                            amount = if (showRewards) Card.getReward(index) else 1
                        }
                    })
                }
            }
        }

        forEachItemSlot { slot, index ->
            if (team?.completed?.getOrNull(index) ?: false)
            {
                val bingoTimes = team.slotsBingoTimes.getOrNull(index) ?: 0
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
            } else
            {
                val reward = Card.getReward(index)
                set(slot, buildItem(Card.items[index].material).modifyMeta<ItemMeta> {
                    modifyLore { asLangTextList("card-item-lore", reward) }
                    setMaxStackSize(99)
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
                    team?.players?.forEach {
                        it.sendLang(
                            node,
                            Card.items[index].material.getI18nName(this@openCard)
                        )
                    }
                }
            }
        }
    }
}

fun forEachItemSlot(action: (Int, Int) -> Unit)
{
    for (i in 0 until 5)
    {
        for (j in 0 until 5)
        {
            val slot = i * 9 + j + 2
            val index = i * 5 + j
            action(slot, index)
        }
    }
}