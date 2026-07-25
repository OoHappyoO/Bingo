package gg.happy.bingo.module.ui

import gg.happy.bingo.module.conf.Conf
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.platform.util.buildItem
import kotlin.math.max

class ItemsUI(val page: Int)
{
    companion object
    {
        val pages = mutableListOf<ItemsUI>()
        var listener: ProxyListener? = null

        fun init()
        {
            if (listener == null)
                listener = registerBukkitListener(InventoryClickEvent::class.java) { onInventoryClick(it) }
        }

        fun getUI(page: Int): ItemsUI
        {
            while (pages.size <= page)
                pages.add(ItemsUI(pages.size + 1))
            return pages[page - 1]
        }

        fun onInventoryClick(event: InventoryClickEvent)
        {
            val ui = pages.find { it.inv == event.clickedInventory } ?: return
            when (event.slot)
            {
                in 0..3 -> event.isCancelled = true
                in 4..8 ->
                {
                    event.isCancelled = true
                    getUI(ui.getPage(event.slot)).openFor(event.whoClicked)
                }
            }
        }

        fun save()
        {
            val items = mutableListOf<String>().apply {
                pages.forEach { ui ->
                    for (i in 9..53)
                    {
                        val item = ui.inv.getItem(i)
                        if (item != null)
                            add(item.type.toString())
                    }
                }
            }
            Conf.itemsConf["items"] = items
            Conf.itemsConf.saveToFile()
        }
    }

    val inv = Bukkit.createInventory(null, 54, "Items List [Page $page]").apply {
        for (i in 0..3)
            setItem(
                i,
                buildItem(Material.GRAY_STAINED_GLASS_PANE) {
                    name = " "
                }
            )
        for (i in 4..8)
            setItem(
                i,
                buildItem(Material.CHEST) {
                    name = "Page ${getPage(i)}"
                    amount = getPage(i)
                }
            )
        for (i in 9..53)
        {
            Conf.items.getOrNull(i + page * 45 - 54)?.let {
                setItem(
                    i,
                    buildItem(it)
                )
            }
        }
    }

    fun openFor(player: HumanEntity)
    {
        player.openInventory(inv)
    }

    fun getPage(slot: Int) = slot - 6 + max(page, 3)
}