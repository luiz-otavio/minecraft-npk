package io.github.luizotavio.npk

import io.github.luizotavio.npk.skin.Skin
import io.github.luizotavio.npk.touch.TouchingHandler
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

abstract class NPC(
    val plugin: Plugin,
    val id: String,
    val location: Location,
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
     * @param index Index of the line to remove
     */
    abstract fun removeLine(index: Int)

    /**
     * Retrieve the bukkit-entity from this NPC
     * @return LivingEntity from this NPC
     */
    abstract fun getEntity(): LivingEntity

    /**
     * Retrieve the bukkit-entity from this NPC
     * @return TouchingHandler from this NPC
     */
    abstract fun getTouchingHandler(): TouchingHandler?

    /**
     * Change the current skin of the NPC
     * @param skin New skin of the NPC
     */
    abstract fun updateSkin(skin: Skin)

    /**
     * Make the NPC destroy itself
     */
    abstract fun destroy()

    /**
     * Change the current touching handler of the NPC
     * @param touchingHandler New touching handler of the NPC
     */
    abstract fun setTouchingHandler(touchingHandler: TouchingHandler)
}