class Store <S : State> {

    var currentState: S? private set
    private var isDispatching: Boolean
    private val subscribers: MutableList<(S) -> Unit>
    private val reducer: (Action, S?) -> S
    private val dispatcherFunction: (Action) -> Action

    constructor(initialState: S?, reducer: (Action, S?) -> S, vararg middleware: (((Action) -> Action)?, () -> S?) -> ((Action) -> Action) -> ((Action) -> Action)) {
        this.reducer = reducer
        this.subscribers = mutableListOf()
        this.currentState = initialState
        this.isDispatching = false
        this.dispatcherFunction = middleware
                .reversed()
                .fold({ action -> defaultDispatch(action) }) { dispatchFunction, middleware -> middleware({ action -> defaultDispatch(action) }, { currentState })(dispatchFunction) }
    }

    // DispatchFunction =  (A) -> A
    // GetState = () -> StateType?
    // Middleware = (((A) -> A)?, () -> S?) -> ((A) -> A) -> ((A) -> A)

    /**
     * Dispatches an action. It is the only way to trigger a state change.
     *
     * The `reducer` function, used to create the store, will be called with the
     * current state and the given `action`. Its return value will
     * be considered the **next** state of the tree, and the change listeners
     * will be notified.
     *
     *  @param action An object representing “what changed”. It is
     * a good idea to keep actions serializable so you can record and replay user
     * sessions, or use the time travelling. It is a good idea to use
     * string constants for action types.
     *
     * @returns the same action object you dispatched.
     *
     */
    private fun defaultDispatch(action: Action): Action {
        if (this.isDispatching) {
            throw IllegalStateException("Reducers may not dispatch actions")
        }

        try {
            this.isDispatching = true
            this.currentState = this.reducer(action, this.currentState)
        } finally {
            this.isDispatching = false
        }

        // Notify all subscribers
        subscribers.forEach { it(this.currentState!!) }

        return action
    }

    fun dispatch(action: Action): Action {
        return dispatcherFunction(action)
    }

    /**
     * Adds a change listener. It will be called any time an action is dispatched,
     * and some part of the state tree may potentially have changed.
     *
     * @param subscriber function to be invoked on every dispatch.
     * @returns A function to remove this change listener.
     */
    fun subscribe(subscriber: (S) -> Unit): Subscription {
        this.subscribers.add(subscriber)
        return SubscriptionImpl { this.subscribers.remove(subscriber) }
    }
}