package io.github.luizotavio.npk.listener

import io.github.luizotavio.npk.NPC
import io.github.luizotavio.npk.NPKFramework
import io.github.luizotavio.npk.event.ClickType
import io.github.luizotavio.npk.event.PlayerInteractAtNPCEvent
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerQuitEvent

class NPCHandler(
    private val framework: NPKFramework
) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        for (npc in framework.collection) {
            if (npc.canSee(player)) {
                npc.hideTo(player)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractAtEntityEvent) {
        val entity = event.rightClicked

        val npc = getOrNull(entity) ?: return

        PlayerInteractAtNPCEvent(npc, event.player, ClickType.RIGHT).call()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        val entity = event.entity

        if (damager !is Player) return
        if (entity !is Player) return

        val npc = getOrNull(entity) ?: return

        PlayerInteractAtNPCEvent(npc, damager, ClickType.LEFT).call()
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onInteractAtNPC(event: PlayerInteractAtNPCEvent) {
        val npc = event.npc

        val handler = npc.getTouchingHandler() ?: return

        handler.invoke(npc, event.clickType, event.player)
    }

    private fun getOrNull(entity: Entity): NPC? {
        val metadataValues = entity.getMetadata("NPC")

        return if (metadataValues.isEmpty()) null else metadataValues[0].value() as NPC
    }

}