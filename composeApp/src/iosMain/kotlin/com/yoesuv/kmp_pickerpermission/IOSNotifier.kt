package com.yoesuv.kmp_pickerpermission

import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter
import platform.Foundation.NSUUID
import platform.darwin.NSObject
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import kotlin.native.concurrent.ThreadLocal

class IOSNotifier : PushNotifier {
    override fun showNotification(title: String, message: String) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        ensureSetup(center)

        val content = UNMutableNotificationContent().apply {
            // Set content properties explicitly (use setters to match platform interop)
            this.setTitle(title)
            this.setBody(message)
            this.setSound(UNNotificationSound.defaultSound())
        }

        // Use immediate delivery like the reference implementation (no trigger)
        val trigger = null
        val requestId = NSUUID().UUIDString
        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = requestId,
            content = content,
            trigger = trigger
        )

        center.addNotificationRequest(request) { error ->
            if (error != null) {
                println("IOSNotifier error: ${error.localizedDescription}")
            }
        }
    }

    private fun ensureSetup(center: UNUserNotificationCenter) {
        // Set delegate once to allow foreground presentation (banner/alert + sound)
        if (!ForegroundDelegate.isInstalled) {
            center.delegate = ForegroundDelegate.instance
            ForegroundDelegate.isInstalled = true
        }

        // Request authorization (idempotent; system only prompts once)
        val options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        center.requestAuthorizationWithOptions(options) { granted, error ->
            if (error != null) {
                println("IOSNotifier auth error: ${error.localizedDescription}")
            } else {
                println("IOSNotifier auth granted: $granted")
            }
        }
    }
}

// Simple singleton delegate to present notifications while the app is in the foreground
@ThreadLocal
private object ForegroundDelegate {
    var isInstalled: Boolean = false

    // Explicitly declare the property type as the protocol so assignment to center.delegate works
    val instance: UNUserNotificationCenterDelegateProtocol =
        object : NSObject(), UNUserNotificationCenterDelegateProtocol {
            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                willPresentNotification: UNNotification,
                withCompletionHandler: (options: UNNotificationPresentationOptions) -> Unit
            ) {
                // Use Alert + Sound for broad compatibility (works on iOS 10+). Banner maps to Alert here.
                withCompletionHandler(UNNotificationPresentationOptionAlert or UNNotificationPresentationOptionSound)
            }
        }
}
