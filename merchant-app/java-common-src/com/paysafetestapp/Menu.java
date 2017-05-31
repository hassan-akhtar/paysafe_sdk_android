package com.paysafetestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.paysafetestapp.androidpay.PublicKeyActivity;
import com.paysafetestapp.utils.Constants;
import com.paysafetestapp.utils.Utils;

/**
 * The Class Menu.
 * 
 * @author manisha.rani
 * @since 26-06-2015
 */
public class Menu extends Activity {
	// Button
	private Button mButWithAndroidPay;
	private Button mSingleUseToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		init();
	}

	/**
	 * This method is called when initialize UI
	 *
	 * @return Void
	 *
	 */

	private void init() {
		mButWithAndroidPay = (Button) findViewById(R.id.btn_android_pay);
		mSingleUseToken = (Button) findViewById(R.id.btn_single_use_token);

		mButWithAndroidPay.setOnClickListener(mClickListener);
		mSingleUseToken.setOnClickListener(mClickListener);
	}

	/**
	 * This method is called to check NFC
	 *
	 * @return Boolean value for checking whether NFC setting is ON/OFF.
	 *
	 */
	private boolean isNFC() {
		boolean isNFCAvailable = Utils.isNFCAvailable(PaysafeApplication.mApplicationContext);
		boolean isOnline = Utils.isOnline(PaysafeApplication.mApplicationContext);
		if (isNFCAvailable) {
			return true;
		} else if (isOnline) {
			return true;
		}
		return false;
	} // end of isNFC()

	/**
	 * This method is called when button click listener
	 *
	 * @return Void
	 *
	 */
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btn_android_pay:
					// check NFC
					if (!isNFC()) {
						Log.v(Constants.TAG_LOG, "NFC check.");
						Toast.makeText(getApplicationContext(),	Constants.NO_NFC_SUPPORT, Toast.LENGTH_LONG).show();
						return;
					} else {
						Log.v(Constants.TAG_LOG, "NFC check. 2 ");
					}

					Intent intentAndroidPay = new Intent(Menu.this,PublicKeyActivity.class);
					startActivity(intentAndroidPay);
					finish();
					break;
				case R.id.btn_single_use_token:
					Intent intentSignleUseToken = new Intent(Menu.this,CardDetails.class);
					startActivity(intentSignleUseToken);
					finish();
					break;
				default:
					break;
			}
		}
	};
}


