package io.github.luizotavio.npk.registry

import io.github.luizotavio.npk.npc.NPC
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

interface NPKRegistry {

  fun register(@NotNull id: String, @NotNull npc: NPC): Boolean

  fun contains(@NotNull id: String) : Boolean

  @Nullable
  fun unregister(@NotNull id: String) : NPC?

  @NotNull
  fun getRegisteredNPCs(): List<NPC>
}
