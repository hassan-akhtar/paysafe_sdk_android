package com.paysafetestapp;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * The Class Menu.
 * 
 * @author manisha.rani
 * @since 26-06-2015
 */
public class Menu extends Activity {
	// Button
	private Button mSingleUseTokenButton;

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
		mSingleUseTokenButton =(Button) findViewById(R.id.btn_SingleUseToken);
		mSingleUseTokenButton.setOnClickListener(mClickListener);
	}

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
			case R.id.btn_SingleUseToken:
				Intent intentSingleUseToken = new Intent(Menu.this,Checkout.class);
				startActivity(intentSingleUseToken);
				finish();
				break;
				default:
				break;
			}
		}
	};
}


