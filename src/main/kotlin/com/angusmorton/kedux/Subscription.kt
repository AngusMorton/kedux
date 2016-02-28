package com.angusmorton.kedux

/**
 * Used to allow unsubscribing from the [Store].
 */
interface Subscription {
    /**
     * Indicates whether this [Subscription] is unsubscribed.
     */
    var isUnsubscribed: Boolean

    /**
     * Stops the subscriber from receiving notifications about state changes.
     */
    fun unsubscribe()

    companion object {
        fun create(unsubscribe: () -> Unit): Subscription {
            return SubscriptionImpl(false, unsubscribe);
        }
    }
}