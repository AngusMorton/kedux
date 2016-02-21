import com.angusmorton.kedux.Action

val ACTION_INCREMENT = "ACTION_INCREMENT"
val ACTION_DECREMENT = "ACTION_DECREMENT"

data class CounterAction(val type: String) : Action