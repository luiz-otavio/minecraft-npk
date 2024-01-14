package io.github.luizotavio.npk.eventbus

import io.github.luizotavio.npk.eventbus.event.NPKEvent
import org.jetbrains.annotations.NotNull
import kotlin.reflect.KClass

typealias NPKEventCallback<T> = (T) -> Unit

interface NPKEventBus {

  fun <T : NPKEvent> listenTo(
    @NotNull clazz: KClass<T>,
    @NotNull priority: Priority,
    @NotNull callback: NPKEventCallback<T>
  ): Boolean

  fun isListeningTo(@NotNull clazz: KClass<NPKEvent>): Boolean

  fun unregister(@NotNull clazz: KClass<NPKEvent>): Boolean

  fun dispatch(@NotNull event: NPKEvent): Boolean

  fun findListeningCallbacks(@NotNull clazz: KClass<NPKEvent>): List<NPKEventCallback<NPKEvent>>

  enum class Priority {
    HIGHEST,
    HIGH,
    NORMAL,
    LOW,
    LOWEST,
  }
}

inline fun <reified T : NPKEvent> NPKEventBus.listenTo(
  @NotNull priority: NPKEventBus.Priority,
  @NotNull noinline callback: NPKEventCallback<T>
) = listenTo(T::class, priority, callback)
