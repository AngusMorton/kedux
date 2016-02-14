import org.junit.Test

import org.junit.Assert.*

class ReducerTest {

    @Test
    fun testReducer() {
        val initialState = TestState(0)
        val counterReducer = { action: TestAction, state: TestState? ->
                if (state == null) {
                    TestState(0)
                } else {
                    when (action.type) {
                        PLUS_ACTION -> state.copy(value = state.value + action.by)
                        MINUS_ACTION -> state.copy(value = state.value - action.by)
                        else -> state
                    }
                }
        }

        val state = counterReducer(TestAction(NO_ACTION, 0), initialState)
        assertSame(initialState, state)
    }

    @Test
    fun testCombineReducers() {
        val initialState = TestState(0)
        val plusOne = createReducer<TestState> { action, state -> state?.copy(value = state.value + 1) ?: TestState(0) }
        val plusTwo = { action: Action, state: TestState? -> state?.copy(value = state.value + 2) ?: TestState(0) }

        val combinedReducers = combineReducers(plusOne, plusTwo)
        val newState = combinedReducers(TestAction(PLUS_ACTION, 1), initialState)

        assertNotEquals(newState, initialState)
        assertEquals(3, newState.value)
    }
}