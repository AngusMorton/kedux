/**
 * Creates a reducer function
 *
 * TODO: verify the reducer function is correct before returning. e.g. Send it an action with a null state.
 */
fun <S : State> createReducer(reducerFunction : (Action, S?) -> S) : (Action, S?) -> S = reducerFunction

/**
 * Turns a number of reducer functions, into a single reducer function.
 * It will call every child reducer, and gather their results into the state.
 *
 * @param reducers A number of reducer functions that need to be combined into one.
 * A reducer should return their initial state if the state passed to them was undefined,
 * and the current state for any unrecognized action.
 *
 * @returns A reducer function that invokes every reducer.
 */
fun <S : State> combineReducers(vararg reducers: (Action, S?) -> S): (Action, S?) -> S {
    return { action, state -> reducers.fold(state) { currentState, reducer -> reducer(action, currentState) }
            ?: throw IllegalStateException("A reducer should return their initial state if the state passed to them was undefined, and the current state for any unrecognized action.") }
}