package com.angusmorton.kedux

import com.angusmorton.kedux.StoreImpl
import com.angusmorton.kedux.createMiddleware
import com.angusmorton.kedux.createReducer
import org.junit.Assert
import org.junit.Test

class MiddlewareTest {

    private val counterReducer = createReducer<TestState> { state, action ->
        if (action is TestAction) {
            when (action.type) {
                PLUS_ACTION -> state.copy(value = state.value + action.by)
                MINUS_ACTION -> state.copy(value = state.value - action.by)
                RESET_ACTION -> state.copy(value = 0)
                else -> state
            }
        } else {
            TestState(0)
        }
    }

    private val incrementPlusMiddleware = createMiddleware { dispatch, state, next, action ->
        if (action is TestAction && action.type.equals(PLUS_ACTION)) {
            next(action.copy(by = action.by + 1))
        } else {
            next(action)
        }
    }

    private val stateAccessingMiddleware = createMiddleware { dispatch, state, next, action ->
        if (state is TestState && state.value > 5 && action is TestAction && action.type.equals(PLUS_ACTION)) {
            dispatch?.invoke(TestAction(RESET_ACTION, 0))
            next(TestAction(NO_ACTION, 0))
        } else {
            next(action)
        }
    }

    @Test
    fun testMiddlewareCanModifyAction() {
        val initialState = TestState(0)
        val store = StoreImpl(initialState, counterReducer, incrementPlusMiddleware)

        val subscription = store.subscribe { state ->
            // We will have received the com.angusmorton.kedux.getPLUS_ACTION 5 which the increment plus middleware will have incremented.
            Assert.assertEquals(6, state.value)
        }

        store.dispatch(TestAction(PLUS_ACTION, 5))

        subscription.unsubscribe()
    }

    @Test
    fun testMiddlewareCanAccessState() {
        val initialState = TestState(0)
        val store = StoreImpl(initialState, counterReducer, incrementPlusMiddleware, stateAccessingMiddleware)

        store.dispatch(TestAction(PLUS_ACTION, 5))
        Assert.assertEquals(6, store.currentState().value)

        store.dispatch(TestAction(PLUS_ACTION, 5))
        Assert.assertEquals(0, store.currentState().value)
    }
}