import org.junit.Assert.*
import org.junit.Test

class StoreTest {

    private val counterReducer = createReducer<TestState> { action, state ->
            if (state == null) {
                TestState(0)
            } else if (action is TestAction){
                when (action.type) {
                    PLUS_ACTION -> state.copy(value = state.value + action.by)
                    MINUS_ACTION -> state.copy(value = state.value - action.by)
                    else -> state
                }
            } else {
                state
            }
    }

    @Test
    fun testSubscribersReceiveChanges() {
        val initialState = TestState(0)
        val store = Store(initialState, counterReducer)

        store.dispatch(TestAction(PLUS_ACTION, 1))

        val subscription = store.subscribe { state ->
            // We will have received the PLUS_ACTION 1 and 5.
            assertEquals(6, state.value)
        }

        store.dispatch(TestAction(PLUS_ACTION, 5))

        subscription.unsubscribe()

        store.dispatch(TestAction(PLUS_ACTION, 1))
    }
}