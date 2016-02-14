interface Subscription {
    var isDisposed: Boolean
    fun unsubscribe()
}

internal data class SubscriptionImpl(override var isDisposed: Boolean = false, private val unsubscribeFunction: () -> Unit) : Subscription {
    override final fun unsubscribe() {
        if (!isDisposed) {
            unsubscribeFunction()
            isDisposed = true
        }
    }
}