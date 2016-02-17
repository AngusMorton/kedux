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
        val plusOne = createReducer<TestState> { state, action -> state.copy(value = state.value + 1) }
        val plusTwo = createReducer<TestState> { state, action -> state.copy(value = state.value + 2) }

        val combinedReducers = combineReducers(plusOne, plusTwo)
        val newState = combinedReducers(initialState, TestAction(PLUS_ACTION, 1))

        assertNotEquals(newState, initialState)
        assertEquals(3, newState.value)
    }
}