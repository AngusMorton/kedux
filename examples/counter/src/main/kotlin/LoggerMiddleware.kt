val loggerMiddleware = createMiddleware { dispatch, state, next, action ->
    println(action)
    next(action)
}