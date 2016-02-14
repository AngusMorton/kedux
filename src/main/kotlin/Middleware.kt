
fun createMiddleware(func : (((Action) -> Action)?, State?, (Action) -> Action, Action) -> Action) : (((Action) -> Action)?, () -> State?) -> ((Action) -> Action) -> ((Action) -> Action) {
    return { dispatch, getState -> { next -> { action ->
        func(dispatch, getState(), next, action)
    }}}
}