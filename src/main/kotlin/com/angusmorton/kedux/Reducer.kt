package com.angusmorton.kedux

/**
 * Creates a reducer function. Mostly for auto-correct convenience.
 *
 * TODO: verify the reducer function is correct before returning. e.g. Send it a random action and test it returns the same state
 */
fun <S : State> createReducer(reducerFunction : (S, Action) -> S) : (S, Action) -> S = reducerFunction

/**
 * Turns a number of reducer functions, into a single reducer function.
 * It will call every child reducer, and gather their results into the state.
 *
 * @param reducers A number of reducer functions that need to be combined into one.
 * A reducer should return the current state for any unrecognized action.
 *
 * @returns A reducer function that invokes every reducer.
 */
fun <S : State> combineReducers(vararg reducers: (S, Action) -> S): (S, Action) -> S {
    return { state, action -> reducers.fold(state) { currentState, reducer -> reducer(currentState, action) } }
}