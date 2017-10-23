package com.paysafetestapp;

/**
 * The Class Checkout.
 * 
 * @author manisha.rani
 * @since 26-06-2015
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.paysafetestapp.paywithgoogle.PayWithGoogleActivity;

public class Checkout extends Activity {

	/**
	 * On Create Activity.
	 *
	 * @param savedInstanceState Object of Bundle holding instance state.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout);
		init();
	} // end of onCreate()

	/**
	 * This method is called when initialize UI.
	 */
	private void init() {
		Button mCheckOutButton = (Button) findViewById(R.id.btn_Checkout);
		mCheckOutButton.setOnClickListener(mClickListener);
	} // end of init()

	/**
	 * This method is called when button click listener.
	 */
	private final OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_Checkout:
				Intent intent = new Intent(Checkout.this,PayWithGoogleActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
		}
	}; // end of OnCLickListener

	/**
	 * This method is called when back pressed finished the activity
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.exit(0);

	} // end of onBackPressed()

} // end of class Checkout