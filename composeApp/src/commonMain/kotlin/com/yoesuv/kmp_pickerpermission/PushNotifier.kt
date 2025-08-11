package com.yoesuv.kmp_pickerpermission

interface PushNotifier {
    fun showNotification(title: String, message: String)
}

// Simple global holder to provide platform-specific notifier to common code
object NotifierHolder {
    private var notifier: PushNotifier? = null

    fun provide(instance: PushNotifier) {
        notifier = instance
    }

    fun getOrNull(): PushNotifier? = notifier
}