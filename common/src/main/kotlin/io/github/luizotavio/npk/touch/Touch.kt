package io.github.luizotavio.npk.touch

import io.github.luizotavio.npk.NPC
import io.github.luizotavio.npk.event.ClickType
import org.bukkit.entity.Player

typealias TouchingHandler = (NPC, ClickType, Player) -> Unit