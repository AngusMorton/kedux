package com.angusmorton.kedux

interface Subscription {
    var isDisposed: Boolean
    fun unsubscribe()

    companion object {
        fun create(unsubscribe: () -> Unit): Subscription {
            return SubscriptionImpl(false, unsubscribe);
        }
    }
}