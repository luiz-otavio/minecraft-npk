package io.github.luizotavio.npk.hologram

import net.minecraft.server.v1_8_R3.EntityArmorStand
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving
import net.minecraft.server.v1_8_R3.WorldServer
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

class CraftHologram(
    location: Location
) {

    companion object {
        const val BETWEEN_DISTANCE = 0.28
    }

    private val armorStands: MutableList<EntityArmorStand> = ArrayList()

    private var current: Location
    private var base: Location

    private lateinit var packetPlayOutEntityDestroy: PacketPlayOutEntityDestroy
    private lateinit var packetPlayOutSpawnEntityLivings: Array<PacketPlayOutSpawnEntityLiving?>

    init {
        base = location.clone()
        current = location.clone()
    }

    fun setLocation(base: Location) {
        this.base = base
    }

    fun appendLines(vararg lines: String) {
        val worldServer = (base.world as CraftWorld).handle

        for (text in lines) {
            createStand(worldServer, text)
        }

        ensurePackets()
    }

    fun showTo(player: Player) {
        val connection = (player as CraftPlayer).handle.playerConnection

        for (packet in packetPlayOutSpawnEntityLivings) {
            connection.sendPacket(packet)
        }
    }

    fun hideTo(player: Player) {
        (player as CraftPlayer).handle.playerConnection.sendPacket(packetPlayOutEntityDestroy)
    }

    fun removeLine(index: Int) {
        if (index >= armorStands.size) return

        armorStands.removeAt(index)

        ensurePackets()
    }

    private fun createStand(worldServer: WorldServer, text: String) {
        EntityArmorStand(worldServer).apply {
            customNameVisible = true
            customName = text

            isInvisible = true
            isSmall = true

            setArms(false)
            setBasePlate(false)
            setGravity(false)

            n(true)

            armorStands.add(this)
        }
    }

    private fun ensurePackets() {
        val size = armorStands.size

        val ids = IntArray(size)

        packetPlayOutSpawnEntityLivings = arrayOfNulls(size)

        for (index in 0 until size) {
            val armorStand = armorStands[index]

            packetPlayOutSpawnEntityLivings[index] = PacketPlayOutSpawnEntityLiving(armorStand)

            ids[index] = armorStand.id
        }

        packetPlayOutEntityDestroy = PacketPlayOutEntityDestroy(*ids)
    }

    fun ensurePosition(players: Set<Player>) {
        current.y = base.y + BETWEEN_DISTANCE * armorStands.size + 1F

        synchronized(armorStands) {
            for (entityArmorStand in armorStands) {
                entityArmorStand.setPosition(current.x, current.y, current.z)

                current.subtract(0.0, BETWEEN_DISTANCE, 0.0)
            }

            for (player in players) {
                val playerConnection = (player as CraftPlayer).handle.playerConnection

                playerConnection.sendPacket(packetPlayOutEntityDestroy)

                for (spawnEntityLiving in packetPlayOutSpawnEntityLivings) {
                    playerConnection.sendPacket(spawnEntityLiving)
                }
            }
        }
    }
}