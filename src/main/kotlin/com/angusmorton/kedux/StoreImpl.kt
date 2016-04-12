package com.angusmorton.kedux

import java.util.concurrent.atomic.AtomicBoolean

internal class StoreImpl <S, A>(
        override var state: S,
        private val reducer: (S, A) -> S,
        private val isDispatching: AtomicBoolean = AtomicBoolean(false),
        private val subscribers: MutableList<(S) -> Unit> = mutableListOf()
) : Store<S, A> {

    override fun dispatch(action: A): A {
        if (this.isDispatching.get()) {
            throw IllegalStateException("Already dispatching - Check that you're not dispatching an action inside a Reducer, and check that may not dispatch actions")
        }

        try {
            this.isDispatching.set(true)
            this.state = this.reducer(this.state, action)
        } finally {
            this.isDispatching.set(false)
        }

        // Notify all subscribers
        subscribers.forEach { it(this.state) }

        return action
    }

    override fun subscribe(subscriber: (S) -> Unit): Subscription {
        this.subscribers.add(subscriber)
        return Subscription.create { this.subscribers.remove(subscriber) }
    }
}