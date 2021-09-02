import react.rawUseState
import kotlin.reflect.KProperty

typealias RSetStateUpdater<T> = ((value: T) -> T) -> Unit

class RStateUpdaterDelegate<T>(
  private val state: T,
  private val setState: RSetStateUpdater<T>
) {
  operator fun component1(): T = state
  operator fun component2(): RSetStateUpdater<T> = setState

  operator fun getValue(
    thisRef: Nothing?,
    property: KProperty<*>
  ): T = state

  operator fun setValue(
    thisRef: Nothing?,
    property: KProperty<*>,
    valueUpdater: (T) -> T
  ) {
    setState{ valueUpdater(state) }
  }
}
inline fun <T> useStateUpdater(initValue: T): RStateUpdaterDelegate<T> {
  val (state, setState) = rawUseState(initValue)
  return RStateUpdaterDelegate(state as T, setState as RSetStateUpdater<T>)
}