package com.angusmorton.kedux

/**
 * Turns a number of reducer functions, into a single reducer function.
 * It will call every child reducer, and gather their results into the state.
 *
 * @param reducers A number of reducer functions that need to be combined into one.
 *
 * @returns A reducer function that invokes every reducer.
 */
fun <A, S> combineReducers(vararg reducers: (S, A) -> S): (S, A) -> S =
        { state, action ->
            reducers.fold(state) { currentState, reducer -> reducer(currentState, action) }
        }