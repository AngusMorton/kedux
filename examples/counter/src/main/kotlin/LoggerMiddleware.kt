import com.angusmorton.kedux.createMiddleware

val loggerMiddleware = createMiddleware { dispatch, state, next, action ->
    println(action)
    next(action)
}