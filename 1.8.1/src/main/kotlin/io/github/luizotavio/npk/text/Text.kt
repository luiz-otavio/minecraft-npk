@file:JvmName("Text")

package io.github.luizotavio.npk.text

import org.bukkit.ChatColor

fun translate(text: String): String = ChatColor.translateAlternateColorCodes('&', text)
