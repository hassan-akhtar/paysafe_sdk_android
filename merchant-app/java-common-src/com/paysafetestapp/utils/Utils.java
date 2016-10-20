package com.paysafetestapp.utils;

/**
 * The Class Utils
 * 
 * @author manisha.rani
 * @since 26-06-2015
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	// progress Dialog
	private static ProgressDialog mProgressDialog;

	/**
	 * Check whether or not the String is empty.
	 * 
	 * @param source String source.
	 * @return Boolean value to check whether or not the string is empty.
	 */
	public static boolean isEmpty(String source) {
		return (null == source || source.trim().equals(""));
	} // end ff isEmpty()

	/**
	 * Checks Network Availability.
	 *
	 * @param context Context.
	 * @return Boolean value to check whether or not Network is available.
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean isInternetAvailable = false;

		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();

			if (networkInfo != null && (networkInfo.isConnected())
					&& networkInfo.isAvailable()) {
				isInternetAvailable = true;
			}
		} catch (Exception exception) {
				exception.printStackTrace();
		}
		return isInternetAvailable;
	} // end of isNetworkAvailable()

	/**
	 * This Method uses ConnectivityManager to checks if connectivity exists or is in the process
	 * of being established.
	 * But it will not guarantee the instant availability.
	 *
	 * @param context Context.
	 * @return Boolean value to check whether or not connectivity is available.
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	} // end of isOnline()

	/**
	 * Start Progress Dialog.
	 * Displays process dialog when the application is communicating to the server or
	 * processing some transactions.
	 */
	public static void startProgressDialog(Context context, String message) {
		mProgressDialog = ProgressDialog.show(context, "", message);
	} // end of startProgressDialog()

	/**
	 * Stop Progress Dialog.
	 * Process dialog stops when on-going process is completed.
	 */
	public static void stopProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;

		}
	} // end of stopProgressDialog()

	/**
	 * Show Alert Dialog.
	 * 
	 * @param alertMessage Alter message to be displayed on the Alert Dialog.
	 * @param context Context.
	 */
	public static void showDialogAlert(String alertMessage, Context context) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		alertDialog.show();
	} // end of showDialogAlert()
} // end of class Utils
