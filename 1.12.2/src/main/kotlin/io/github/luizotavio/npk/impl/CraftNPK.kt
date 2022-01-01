package io.github.luizotavio.npk.impl

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import io.github.luizotavio.npk.NPC
import io.github.luizotavio.npk.hologram.CraftHologram
import io.github.luizotavio.npk.skin.Skin
import io.github.luizotavio.npk.text.translate
import net.minecraft.server.v1_12_R1.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboard
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import java.lang.reflect.Field
import java.util.*

class CraftNPK(
    plugin: Plugin,
    id: String,
    location: Location,
    skin: Skin? = null,
) : NPC(
    plugin,
    id,
    location,
    skin
) {

    companion object {
        val MINECRAFT_SERVER = MinecraftServer.getServer()
        val WORLD_CLASS = World::class.java
        val CRAFT_SCOREBOARD = (Bukkit.getScoreboardManager().mainScoreboard as CraftScoreboard).handle
    }

    private val packetPlayOutPlayerInfoAdd: PacketPlayOutPlayerInfo
    private var packetPlayOutPlayerInfoRemove:PacketPlayOutPlayerInfo
    private val packetPlayOutNamedEntitySpawn: PacketPlayOutNamedEntitySpawn
    private val packetPlayOutEntityDestroy: PacketPlayOutEntityDestroy
    private val packetPlayOutEntityHeadRotation: PacketPlayOutEntityHeadRotation

    private val packetPlayOutScoreboardTeamRemove: PacketPlayOutScoreboardTeam
    private val packetPlayOutScoreboardTeamCreate: PacketPlayOutScoreboardTeam
    private val packetPlayOutScoreboardTeamUpdate: PacketPlayOutScoreboardTeam

    private var nmsPlayer: EntityPlayer
    private var scoreboardTeam: ScoreboardTeam

    private val visibility = hashSetOf<Player>()

    private val hologram: CraftHologram

    init {
        val worldServer = (location.world as CraftWorld).handle

        val gameProfile: GameProfile = createProfile(skin)

        nmsPlayer = EntityPlayer(
            MINECRAFT_SERVER,
            worldServer,
            gameProfile,
            PlayerInteractManager(worldServer)
        )

        nmsPlayer.listName = ChatComponentText(
            translate(id)
        )

        nmsPlayer.setLocation(
            location.x,
            location.y,
            location.z,
            location.yaw,
            location.pitch
        )

        getEntity().setMetadata("NPC", FixedMetadataValue(plugin, this))

        val entityIntHashMap = getCollection(worldServer) ?: throw IllegalStateException("WorldServer.entityList is null")

        entityIntHashMap.a(nmsPlayer.id, nmsPlayer)

        packetPlayOutEntityDestroy = PacketPlayOutEntityDestroy(nmsPlayer.id)
        packetPlayOutPlayerInfoAdd = PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, nmsPlayer)
        packetPlayOutPlayerInfoRemove = PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, nmsPlayer)
        packetPlayOutNamedEntitySpawn = PacketPlayOutNamedEntitySpawn(nmsPlayer)
        packetPlayOutEntityHeadRotation =
            PacketPlayOutEntityHeadRotation(nmsPlayer, (nmsPlayer.yaw * 256 / 356).toInt().toByte())

        scoreboardTeam = ScoreboardTeam(CRAFT_SCOREBOARD, id)

        scoreboardTeam.nameTagVisibility = ScoreboardTeamBase.EnumNameTagVisibility.NEVER

        packetPlayOutScoreboardTeamRemove = PacketPlayOutScoreboardTeam(scoreboardTeam, 1)
        packetPlayOutScoreboardTeamCreate = PacketPlayOutScoreboardTeam(scoreboardTeam, 0)

        packetPlayOutScoreboardTeamUpdate = PacketPlayOutScoreboardTeam(
            scoreboardTeam,
            Collections.singletonList(id), 3
        )

        hologram = CraftHologram(location)
    }

    override fun showTo(vararg players: Player) {
        notify(
            players as Array<Player>,
            packetPlayOutPlayerInfoAdd, packetPlayOutNamedEntitySpawn, packetPlayOutPlayerInfoRemove,
            packetPlayOutScoreboardTeamRemove, packetPlayOutScoreboardTeamCreate, packetPlayOutScoreboardTeamUpdate,
            packetPlayOutEntityHeadRotation
        )

        visibility.addAll(
            players
        )

        for (player in players) {
            hologram.showTo(player)
        }
    }

    override fun hideTo(vararg players: Player) {
        notify(players as Array<Player>, packetPlayOutPlayerInfoRemove, packetPlayOutEntityDestroy)

        for (player in players) {
            hologram.hideTo(player)

            visibility.remove(player)
        }
    }

    override fun teleport(location: Location) {
        nmsPlayer.setLocation(
            location.x,
            location.y,
            location.z,
            location.yaw,
            location.pitch
        )

        hologram.setLocation(location)

        synchronized(visibility) {
            notify(
                visibility.toTypedArray(),
                packetPlayOutEntityDestroy,
                packetPlayOutNamedEntitySpawn,
                packetPlayOutEntityHeadRotation
            )
        }

        hologram.ensurePosition(visibility)
    }

    override fun canSee(player: Player): Boolean = visibility.contains(player)

    override fun addLine(line: String) {
        hologram.appendLines(line)

        hologram.ensurePosition(visibility)
    }

    override fun removeLine(index: Int) {
        hologram.removeLine(index)

        hologram.ensurePosition(visibility)
    }

    override fun getEntity(): LivingEntity = nmsPlayer.bukkitEntity

    override fun updateSkin(skin: Skin) {
        val gameProfile = nmsPlayer.profile

        gameProfile.properties.put(
            "textures",
            Property("textures", skin.texture, skin.signature)
        )

        synchronized(visibility) {
            notify(
                visibility.toTypedArray(), packetPlayOutPlayerInfoRemove, packetPlayOutEntityDestroy,
                packetPlayOutPlayerInfoAdd, packetPlayOutNamedEntitySpawn
            )
        }
    }

    override fun destroy() {
        for (player in visibility) {
            hologram.hideTo(player)

            val connection = (player as CraftPlayer).handle.playerConnection

            connection.sendPacket(packetPlayOutPlayerInfoRemove)
            connection.sendPacket(packetPlayOutEntityDestroy)
        }

        visibility.clear()
    }

    private fun createProfile(skin: Skin?): GameProfile {
        val gameProfile = GameProfile(
            UUID.randomUUID(),
            id
        )

        if (skin != null) {
            gameProfile.properties.put(
                "textures",
                Property("textures", skin.texture, skin.signature)
            )
        }

        return gameProfile
    }

    private fun notify(players: Array<Player>, vararg packets: Packet<*>) {
        for (player in players) {
            val playerConnection = (player as CraftPlayer).handle.playerConnection

            for (packet in packets) {
                playerConnection.sendPacket(packet)
            }
        }
    }

    private fun getCollection(server: WorldServer): IntHashMap<Entity>? {
        try {
            val field: Field = WORLD_CLASS.getDeclaredField("entitiesById")

            field.isAccessible = true

            return field[server] as IntHashMap<Entity>
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return null
    }


}