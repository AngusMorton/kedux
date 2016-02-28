package com.angusmorton.kedux

import java.util.concurrent.atomic.AtomicBoolean

internal class StoreImpl <S : State> : Store<S> {
    private var currentState: S
    private var isDispatching: AtomicBoolean
    private val subscribers: MutableList<(S) -> Unit>
    private val reducer: (S, Action) -> S

    constructor(initialState: S, reducer: (S, Action) -> S) {
        this.reducer = reducer
        this.subscribers = mutableListOf()
        this.isDispatching = AtomicBoolean(false)
        this.currentState = initialState
    }

    override fun currentState(): S {
        return currentState
    }

    override fun dispatch(action: Action): Action {
        if (this.isDispatching.get()) {
            throw IllegalStateException("Already dispatching - Check that you're not dispatching an action inside a Reducer, and check that may not dispatch actions")
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

    override fun subscribe(subscriber: (S) -> Unit): Subscription {
        this.subscribers.add(subscriber)
        return Subscription.create { this.subscribers.remove(subscriber) }
    }
}