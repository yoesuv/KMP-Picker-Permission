package com.yoesuv.kmp_pickerpermission

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = run {
    // Provide platform notifier to common code
    NotifierHolder.provide(IOSNotifier())
    ComposeUIViewController { App() }
}