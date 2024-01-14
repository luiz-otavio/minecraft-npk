package io.github.luizotavio.npk

import io.github.luizotavio.npk.eventbus.NPKEventBus
import io.github.luizotavio.npk.npc.NPC
import io.github.luizotavio.npk.npc.position.Position
import io.github.luizotavio.npk.npc.skin.Skin
import io.github.luizotavio.npk.npc.visibility.Visibility
import io.github.luizotavio.npk.registry.NPKRegistry
import org.jetbrains.annotations.NotNull

interface NPKFrame<T> {

  fun getProvider(): T

  @NotNull
  fun getRegistry(): NPKRegistry

  @NotNull
  fun getEventBus(): NPKEventBus

  @NotNull
  fun createNPC(@NotNull id: String): NPC

  @NotNull
  fun createNPC(
    @NotNull id: String,
    @NotNull position: Position
  ): NPC

  @NotNull
  fun createNPC(
    @NotNull id: String,
    @NotNull position: Position,
    @NotNull skin: Skin
  ): NPC

  @NotNull
  fun createNPC(
    @NotNull id: String,
    @NotNull position: Position,
    @NotNull skin: Skin,
    @NotNull visibilityStatus: Visibility.VisibilityStatus
  ): NPC

}
