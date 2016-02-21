package com.angusmorton.kedux

import java.util.concurrent.atomic.AtomicBoolean

internal class StoreImpl <S : State> : Store<S> {
    private var currentState: S
    private var isDispatching: AtomicBoolean
    private val subscribers: MutableList<(S) -> Unit>
    private val reducer: (S, Action) -> S
    private val dispatcherFunction: (Action) -> Action

    constructor(initialState: S, reducer: (S, Action) -> S, vararg middleware: (((Action) -> Action)?, () -> S?) -> ((Action) -> Action) -> ((Action) -> Action)) {
        this.reducer = reducer
        this.subscribers = mutableListOf()
        this.isDispatching = AtomicBoolean(false)
        this.currentState = initialState
        this.dispatcherFunction = middleware
                .reversed()
                .fold({ action -> defaultDispatch(action) }) { dispatchFunction, middleware -> middleware({ action -> defaultDispatch(action) }, { currentState })(dispatchFunction) }
    }

    override fun currentState(): S {
        return currentState
    }

    private fun defaultDispatch(action: Action): Action {
        if (this.isDispatching.get()) {
            throw IllegalStateException("Reducers may not dispatch actions")
        }

        try {
            this.isDispatching.set(true)
            this.currentState = this.reducer(this.currentState, action)
        } finally {
            this.isDispatching.set(false)
        }

        // Notify all subscribers
        subscribers.forEach { it(this.currentState) }

        return action
    }

    override fun dispatch(action: Action): Action {
        return dispatcherFunction(action)
    }

    override fun subscribe(subscriber: (S) -> Unit): Subscription {
        this.subscribers.add(subscriber)
        return Subscription.create { this.subscribers.remove(subscriber) }
    }
}