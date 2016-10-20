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
		Button mBackButton = (Button) findViewById(R.id.btn_back);
		mBackButton.setOnClickListener(mClickListener);
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
				Intent intent = new Intent(Checkout.this,CardDetails.class);
				startActivity(intent);
				finish();
				break;
				
			case R.id.btn_back:
				Intent intentBack = new Intent(Checkout.this,Menu.class);
				startActivity(intentBack);
				finish();
				break;
			default:
				break;
			}
		}
	}; // end of OnCLickListener
} // end of class Checkout