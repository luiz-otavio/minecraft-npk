package io.github.luizotavio.npk.npc

import io.github.luizotavio.npk.npc.position.Position
import io.github.luizotavio.npk.npc.skin.Skin
import io.github.luizotavio.npk.npc.visibility.Visibility
import org.jetbrains.annotations.NotNull

interface NPC {

  @NotNull
  fun getId(): String

  fun show(@NotNull playerName: String)

  fun hidden(@NotNull playerName: String)

  fun spawn(@NotNull position: Position)

  fun move(@NotNull position: Position)

  fun changeSkin(@NotNull skin: Skin)

  @NotNull
  fun getVisibility(): Visibility

  fun isSpawned(): Boolean

  fun destroy();

}
