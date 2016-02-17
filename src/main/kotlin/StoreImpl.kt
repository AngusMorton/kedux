import java.util.concurrent.atomic.AtomicBoolean

internal class StoreImpl <S : State> : Store<S> {

    override var currentState: S
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
        return SubscriptionImpl { this.subscribers.remove(subscriber) }
    }
}