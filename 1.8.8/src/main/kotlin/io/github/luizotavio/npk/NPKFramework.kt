package io.github.luizotavio.npk

import io.github.luizotavio.npk.impl.CraftNPK
import io.github.luizotavio.npk.listener.NPCHandler
import io.github.luizotavio.npk.skin.Skin
import io.github.luizotavio.npk.touch.TouchingHandler
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.Plugin

class NPKFramework(
    private val plugin: Plugin
) {

    init {
        if (!plugin.isEnabled) {
            throw UnsupportedOperationException("Cannot initialize NPCFramework with not enabled plugin.")
        }

        Bukkit.getPluginManager().registerEvents(
            NPCHandler(this),
            plugin
        )
    }

    val collection = hashSetOf<NPC>()

    fun createNPC(id: String, location: Location) = createNPC(id, location, null, null)

    fun createNPC(id: String, location: Location, skin: Skin) = createNPC(id, location, skin, null)

    fun createNPC(id: String, location: Location, touchingHandler: TouchingHandler? = null) = createNPC(id, location, null, touchingHandler)

    fun createNPC(id: String, location: Location, skin: Skin?, touchingHandler: TouchingHandler? = null): NPC =
        CraftNPK(plugin, id, location, skin).apply {
            collection.add(this)

            if (touchingHandler != null) {
                setTouchingHandler(touchingHandler)
            }
        }

    fun unregister(id: String) {
        val npc = getNPC(id) ?: return

        npc.destroy()

        collection.remove(npc)
    }

    fun unregisterAll() {
        for (npc in collection) {
            npc.destroy()
        }

        collection.clear()
    }

    fun getNPC(id: String) = collection.firstOrNull { it.id == id }

}