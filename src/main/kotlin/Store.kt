
interface Store<S: State> {

    var currentState : S

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
    fun dispatch(action: Action): Action

    /**
     * Adds a change listener. It will be called any time an action is dispatched,
     * and some part of the state tree may potentially have changed.
     *
     * @param subscriber function to be invoked on every dispatch.
     * @returns A function to remove this change listener.
     */
    fun subscribe(subscriber: (S) -> Unit): Subscription

    companion object {
        fun <S: State> create(initialState: S, reducer: (S, Action) -> S, vararg middleware: (((Action) -> Action)?, () -> S?) -> ((Action) -> Action) -> ((Action) -> Action)) : Store<S> {
            return StoreImpl(initialState, reducer, *middleware)
        }
    }
}