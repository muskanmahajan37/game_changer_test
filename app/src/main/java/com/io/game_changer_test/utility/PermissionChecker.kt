package com.io.game_changer_test.utility

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class PermissionChecker {

    companion object checkPermission {

        var isPermissionGranted:Boolean? = false

        fun checkPermission(activity: Activity):Boolean {
            Dexter.withActivity(activity)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) { // do you work now
                            isPermissionGranted = true
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) { // permission is denied permenantly, navigate user to app settings
                            isPermissionGranted = true
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
            return isPermissionGranted!!

        }


        /**
         * Showing Alert Dialog with Settings option
         * Navigates user to app settings
         * NOTE: Keep proper title and message depending on your app
         */
        public fun showSettingsDialog(activity: Activity,title:String,message:String,
                                       positiveButtonText:String,negativeButtonText:String,action:Int) {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(positiveButtonText,
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                    performAction(action,activity)
                })
            builder.setNegativeButton(negativeButtonText,
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builder.show()
        }

        private fun performAction(action: Int, activity: Activity) {
            when(action) {
                (1) ->{
                    openSetting(activity)
                }

            }
        }

        private fun openSetting(activity: Activity) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivityForResult(intent, 101)
        }


    }
}