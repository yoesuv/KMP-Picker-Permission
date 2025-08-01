package com.yoesuv.kmp_pickerpermission.platform

import platform.Foundation.timeIntervalSince1970
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIDatePickerStyle

actual fun pickDate(onDatePicked: (Long) -> Unit) {
    val datePicker = UIDatePicker().apply {
        datePickerMode = UIDatePickerMode.UIDatePickerModeDate
        preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
        translatesAutoresizingMaskIntoConstraints = false
    }

    val alertController = UIAlertController.alertControllerWithTitle(
        title = "Pick a Date",
        message = "\n\n\n\n\n\n\n",
        preferredStyle = UIAlertControllerStyleAlert
    )

    // Set alert controller height to accommodate the date picker
    alertController.view.heightAnchor.constraintEqualToConstant(300.0).active = true

    alertController.view.addSubview(datePicker)

    // Set constraints for the DatePicker
    datePicker.centerXAnchor.constraintEqualToAnchor(alertController.view.centerXAnchor).active = true
    datePicker.centerYAnchor.constraintEqualToAnchor(alertController.view.centerYAnchor).active = true
    datePicker.widthAnchor.constraintEqualToAnchor(
        alertController.view.widthAnchor,
        constant = -20.0
    ).active = true
    datePicker.heightAnchor.constraintEqualToConstant(200.0).active = true

    // Add Done button
    alertController.addAction(
        UIAlertAction.actionWithTitle(
            title = "Done",
            style = UIAlertActionStyleDefault
        ) { _ ->
            val selectedDate = datePicker.date
            // Convert NSDate to milliseconds since epoch
            val milliseconds = (selectedDate.timeIntervalSince1970 * 1000).toLong()
            onDatePicked(milliseconds)
        }
    )

    // Add Cancel button
    alertController.addAction(
        UIAlertAction.actionWithTitle(
            title = "Cancel",
            style = UIAlertActionStyleCancel
        ) { _ ->
            // Do nothing on cancel
        }
    )

    // Present the alert
    UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
        alertController,
        animated = true,
        completion = null
    )
}
