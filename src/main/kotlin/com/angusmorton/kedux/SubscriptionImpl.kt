package com.angusmorton.kedux

internal data class SubscriptionImpl(override var isUnsubscribed: Boolean = false, private val unsubscribeFunction: () -> Unit) : Subscription {
    override final fun unsubscribe() {
        if (!isUnsubscribed) {
            unsubscribeFunction()
            isUnsubscribed = true
        }
    }
}