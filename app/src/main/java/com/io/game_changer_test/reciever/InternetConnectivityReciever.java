package com.io.game_changer_test.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.io.game_changer_test.dialog.DialogParentActivity;

public class InternetConnectivityReciever extends BroadcastReceiver {
	private ConnectivityReceiverListener mConnectivityReceiverListener;

	public InternetConnectivityReciever(ConnectivityReceiverListener listener) {
		mConnectivityReceiverListener = listener;
	}


	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i("devicename" , "++++"+getDeviceName());
		if(getDeviceName().toLowerCase().contains("oppo")){
			if(isConnected(context)){
				try {
					context.sendBroadcast(new Intent("xyz"));
				} catch (Exception e) {
					System.out.println(e);
					//dialogBuilder.create().dismiss();
				}
			}else{
				Intent data = new Intent(context, DialogParentActivity.class);
				data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				data.putExtra("isConnected", "0");
				context.startActivity(data);
			}

		}else{
			mConnectivityReceiverListener.onNetworkConnectionChanged(isConnected(context));

		}

	}

	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	public interface ConnectivityReceiverListener {
		void onNetworkConnectionChanged(boolean isConnected);
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		}
		return capitalize(manufacturer) + " " + model;
	}

	private static String capitalize(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}
		char[] arr = str.toCharArray();
		boolean capitalizeNext = true;

		StringBuilder phrase = new StringBuilder();
		for (char c : arr) {
			if (capitalizeNext && Character.isLetter(c)) {
				phrase.append(Character.toUpperCase(c));
				capitalizeNext = false;
				continue;
			} else if (Character.isWhitespace(c)) {
				capitalizeNext = true;
			}
			phrase.append(c);
		}

		return phrase.toString();
	}
}
