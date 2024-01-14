package io.github.luizotavio.npk.npc.visibility

import org.jetbrains.annotations.NotNull

interface Visibility {

  fun getDefaultVisibilityStatus(): VisibilityStatus

  fun setVisibility(@NotNull playerName: String, @NotNull visibilityStatus: VisibilityStatus): Boolean

  fun getVisibility(@NotNull playerName: String, supplier: () -> VisibilityStatus): VisibilityStatus

  fun getVisibility(@NotNull playerName: String): VisibilityStatus

  enum class VisibilityStatus {
    VISIBLE,
    HIDDEN,
    FILTERED
  }

}
