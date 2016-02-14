val counterReducer = createReducer<CounterState> { action, state ->
    if (state == null) {
        CounterState(0)
    } else if (action is CounterAction) {
        when (action.type) {
            ACTION_DECREMENT -> state.copy(value = state.value - 1)
            ACTION_INCREMENT -> state.copy(value = state.value + 1)
            else -> state
        }
    } else {
        state
    }
}