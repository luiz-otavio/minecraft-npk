package io.github.luizotavio.npk.event

import io.github.luizotavio.npk.NPC
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class NPCEvent(
    val npc: NPC
) : Event(), Cancellable {

    private var isCancelled = false

    companion object {
        val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLER_LIST
    }

    override fun getHandlers(): HandlerList = HANDLER_LIST

    override fun isCancelled(): Boolean = isCancelled

    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }

    fun call() {
        Bukkit.getPluginManager().callEvent(this)
    }
}

/**
 * Called when a player interacts with an NPC.
 */
class PlayerInteractAtNPCEvent(
    npc: NPC,
    val player: Player,
    val clickType: ClickType
) : NPCEvent(npc)

/**
 * Represents the different types of clicks that can be performed on an NPC.
 */
enum class ClickType {
    LEFT,
    RIGHT,
}
