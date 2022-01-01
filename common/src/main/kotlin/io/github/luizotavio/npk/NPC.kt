package io.github.luizotavio.npk

import io.github.luizotavio.npk.skin.Skin
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

abstract class NPC(
    val plugin: Plugin,
    val id: String,
    var skin: Skin? = null,
) {

    /**
     * Make the NPC visible to the player
     * @param player Player to show the NPC to
     */
    abstract fun showTo(vararg player: Player)

    /**
     * Make the NPC invisible to the player
     * @param player Player to hide the NPC from
     */
    abstract fun hideTo(vararg player: Player)

    /**
     * Change the current location of the NPC
     * @param location New location of the NPC
     */
    abstract fun teleport(location: Location)

    /**
     * Check if the NPC is visible to the player
     * @param player Player to check if the NPC is visible to
     */
    abstract fun canSee(player: Player): Boolean

    /**
     * Append to the NPC the given line
     * @param line Line to append
     */
    abstract fun addLine(line: String)

    /**
     * Remove the given line from the NPC
     * @param line Line to remove
     */
    abstract fun removeLine(line: String)

    /**
     * Retrieve the bukkit-entity from this NPC
     * @return LivingEntity from this NPC
     */
    abstract fun getEntity(): LivingEntity

    /**
     * Change the current skin of the NPC
     * @param skin New skin of the NPC
     */
    abstract fun updateSkin(skin: Skin)

    /**
     * Make the NPC destroy itself
     */
    abstract fun destroy()

}