package gg.happy.bingo.module.game.impl

import gg.happy.bingo.module.PlayerData
import gg.happy.bingo.module.game.GameManager
import gg.happy.bingo.module.game.GamePhase
import gg.happy.bingo.module.listener.SneakSwapListener
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.unregisterListener
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.sendLang
import taboolib.platform.util.submit
import java.util.UUID

object Ready : GamePhase
{
    var playerMoveListener: ProxyListener? = null

    override fun onStart()
    {
        SneakSwapListener.register()
        val world = Bukkit.getWorld("world")!!
        val spawn = Location(world, 0.5, (world.getHighestBlockAt(0, 0).y + 48).toDouble(), 0.5, 0.0f, 90.0f)
        Bukkit.getOnlinePlayers().forEach {
            with(it)
            {
                teleport(spawn)
                it.sendLang("ready")
                if (PlayerData.isPlayer(it))
                {
                    gameMode = GameMode.SURVIVAL
                    health = 20.0
                    foodLevel = 20
                    saturation = 6.0f
                    submit(delay = 1) {
                        allowFlight = true
                        isFlying = true
                        flySpeed = 0.0f
                        teleport(spawn)
                    }
                    Bukkit.getOnlinePlayers().forEach { player ->
                        if (PlayerData.isPlayer(player) && player != it)
                            player.hideEntity(BukkitPlugin.getInstance(), it)
                    }
                    PlayerData.get(it)?.team?.players?.let { teammates ->
                        if (teammates.size > 1)
                        {
                            it.sendLang("teammate")
                            teammates.forEach { teammate ->
                                if (teammate != it)
                                    it.sendLang("teammate-format", teammate.name)
                            }
                        }
                    }
                } else
                {
                    gameMode = GameMode.SPECTATOR
                }
                it.sendLang("ready-60s")
            }
        }
        playerMoveListener = registerBukkitListener(PlayerMoveEvent::class.java) {
            if (PlayerData.isPlayer(it.player))
            {
                it.to?.let { to ->
                    it.setTo(it.from.clone().apply {
                        yaw = to.yaw
                        pitch = to.pitch
                    })
                }

            }
        }
        submit(delay = 600) {
            Bukkit.getOnlinePlayers().forEach {
                it.sendLang("ready-30s")
            }
        }
        submit(delay = 1000) {
            Bukkit.getOnlinePlayers().forEach {
                it.sendLang("ready-10s")
            }
        }
        submit(delay = 1100) {
            Bukkit.getOnlinePlayers().forEach {
                it.sendLang("ready-5s")
            }
        }
        submit(delay = 1120) {
            Bukkit.getOnlinePlayers().forEach {
                it.sendLang("ready-4s")
            }
        }
        submit(delay = 1140) {
            Bukkit.getOnlinePlayers().forEach {
                it.sendLang("ready-3s")
            }
        }
        submit(delay = 1160) {
            Bukkit.getOnlinePlayers().forEach {
                it.sendLang("ready-2s")
            }
        }
        submit(delay = 1180) {
            Bukkit.getOnlinePlayers().forEach {
                it.sendLang("ready-1s")
            }
        }
        submit(delay = 1200) { GameManager.phase = Main }
    }

    var slowFallingListener: ProxyListener? = null
    var slowFalling = mutableSetOf<UUID>()

    override fun onEnd()
    {
        playerMoveListener?.let { unregisterListener(it) }
        Bukkit.getOnlinePlayers().forEach {
            with(it)
            {
                if (PlayerData.isPlayer(it))
                {
                    isFlying = false
                    allowFlight = false
                    flySpeed = 1.0f
                    Bukkit.getOnlinePlayers().forEach { player ->
                        if (PlayerData.isPlayer(player) && player != it)
                            player.showEntity(BukkitPlugin.getInstance(), it)
                    }
                    addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, PotionEffect.INFINITE_DURATION, 0))
                    slowFalling.add(uniqueId)
                }
            }
        }
        slowFallingListener = registerBukkitListener(PlayerMoveEvent::class.java) { event ->
            val player = event.player
            if (PlayerData.isPlayer(player) && slowFalling.contains(player.uniqueId) && player.isOnGround)
            {
                player.removePotionEffect(PotionEffectType.SLOW_FALLING)
                slowFalling.remove(player.uniqueId)
            }
        }
    }
}