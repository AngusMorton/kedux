internal val PLUS_ACTION = "PLUS_ACTION"
internal val MINUS_ACTION = "MINUS_ACTION"
internal val RESET_ACTION = "RESET_ACTION"
internal val NO_ACTION = "NO_ACTION"

internal data class TestState(val value: Int) : State

internal data class TestAction(val type: String, val by: Int) : Action