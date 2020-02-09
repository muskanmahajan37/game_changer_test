package com.io.game_changer_test.services

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.io.game_changer_test.dialog.DialogParentActivity
import com.io.game_changer_test.reciever.InternetConnectivityReciever
import com.io.game_changer_test.reciever.InternetConnectivityReciever.ConnectivityReceiverListener

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class InternetServices : JobService(), ConnectivityReceiverListener {
    private var mConnectivityReceiver: InternetConnectivityReciever? = null
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service created")
        mConnectivityReceiver = InternetConnectivityReciever(this)
    }

    /**
     * When the app's NetworkConnectionActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCallback()"
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        registerReceiver(
            mConnectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        return Service.START_STICKY
    }

    override fun onStartJob(params: JobParameters): Boolean {
        Log.i(TAG, "onStartJob$mConnectivityReceiver")
        registerReceiver(
            mConnectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        Log.i(TAG, "onStopJob")
        //   unregisterReceiver(mConnectivityReceiver);
        return true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) { //        String message = isConnected ? "Good! Connected to Internet" : "Sorry! Not connected to internet";
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        if (isConnected) {
            try {
                sendBroadcast(Intent("xyz"))
            } catch (e: Exception) {
                println(e)
                //dialogBuilder.create().dismiss();
            }
        } else {
            try {
                val intent = Intent(this@InternetServices, DialogParentActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("isConnected", "0")
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mConnectivityReceiver);
        stopSelf()
    }

    companion object {
        private val TAG = InternetServices::class.java.simpleName
    }
}