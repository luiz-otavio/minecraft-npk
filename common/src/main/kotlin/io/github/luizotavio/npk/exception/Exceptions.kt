@file:JvmName("NPKExceptionsImpl")

package io.github.luizotavio.npk.exception

import io.github.luizotavio.npk.npc.NPC
import io.github.luizotavio.npk.npc.position.Position
import io.github.luizotavio.npk.npc.skin.Skin
import io.github.luizotavio.npk.npc.visibility.Visibility
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

open class NPKException(@NotNull val npc: NPC, override val message: String?) : Throwable(message)

class NPKCannotSpawnException(@NotNull npc: NPC, @NotNull val position: Position) : NPKException(npc, "Cannot spawn NPC at this position: $position")
class NPKCannotChangeSkinException(@NotNull npc: NPC, @NotNull val skin: Skin) : NPKException(npc, "Cannot change NPC skin with $skin")
class NPKCannotChangeVisibilityException(@NotNull npc: NPC, @NotNull val visibilityStatus: Visibility.VisibilityStatus, @Nullable playerName: String? = "") :
  NPKException(
    npc,
    if (playerName != null) {
      "Cannot change visibility $visibilityStatus for player: $playerName"
    } else {
      "Cannot change default visibility to $visibilityStatus"
    }
  )
class NPKCannotMoveException(@NotNull npc: NPC, @NotNull val position: Position) : NPKException(npc, "Cannot move NPC to $position")
class NPKCannotDestroyException(@NotNull npc: NPC) : NPKException(npc, "Cannot destroy NPC. Is it spawned first?")
