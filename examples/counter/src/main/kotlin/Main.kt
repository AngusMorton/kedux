@file:JvmName("Main")

import com.angusmorton.kedux.Store

class Main {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val store = Store.create(CounterState(), counterReducer, loggerMiddleware)
            store.subscribe { state -> println("main got: $state") }
            store.dispatch(CounterAction(ACTION_INCREMENT))
            store.dispatch(CounterAction(ACTION_INCREMENT))
            store.dispatch(CounterAction(ACTION_INCREMENT))
            store.dispatch(CounterAction(ACTION_INCREMENT))
            store.dispatch(CounterAction(ACTION_INCREMENT))
            store.dispatch(CounterAction(ACTION_DECREMENT))
            store.dispatch(CounterAction(ACTION_DECREMENT))
            store.dispatch(CounterAction(ACTION_DECREMENT))
            store.dispatch(CounterAction(ACTION_DECREMENT))
            store.dispatch(CounterAction(ACTION_DECREMENT))
            store.dispatch(CounterAction(ACTION_DECREMENT))
        }
    }
}