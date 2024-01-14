package io.github.luizotavio.npk.npc.position

interface Position {

  fun getWorldName(): String

  fun getX(): Double

  fun getY(): Double

  fun getZ(): Double

  fun getYaw(): Float

  fun getPitch(): Float

}
