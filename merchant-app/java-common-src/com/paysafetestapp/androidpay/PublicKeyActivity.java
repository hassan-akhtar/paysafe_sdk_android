package com.paysafetestapp.androidpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.paysafetestapp.Menu;
import com.paysafetestapp.R;
import com.paysafetestapp.utils.AppSettings;
import com.paysafetestapp.utils.Utils;

import java.io.IOException;


/**
 * Created by asawari.vaidya on 20-04-2017.
 */

public class PublicKeyActivity extends Activity {

    // Button
    private Button mUse;
    private Button mClear;
    private Button mBackButton;

    // Radio Group
    private RadioGroup rgEnvironment;

    // Radio Button
    private RadioButton rbEnvProd;
    private RadioButton rbEnvTest;

    // EditText
    private EditText mPublicKey;

    // String
    private String mStrPublicKey;
    private String mStrEnvironment;

    // Context
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.androidpaypublickey);

        // Context
        mContext = this;

        try {
            mStrPublicKey = Utils.getProperty("android_pay_public_key", mContext);
        } catch(IOException ioExp) {
            Utils.showDialogAlert("IOException: "+ ioExp.getMessage(), mContext);
        }

        init();
    }

    /**
     * set values in to Shared Preferences
     *
     */
    private void setValuesIntoShardPrf() {
        AppSettings.setBoolean("testRadioButton", rbEnvTest.isChecked());
        AppSettings.setBoolean("prodRadioButton", rbEnvProd.isChecked());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Default Radio Button Values
        rbEnvTest.setChecked(AppSettings.getBoolean("testRadioButton", true));
        rbEnvProd.setChecked(AppSettings.getBoolean("prodRadioButton", false));
    }

    /**
     * This method is called when initialize UI.
     */
    private void init() {
        mUse = (Button) findViewById(R.id.btn_Use);
        mClear = (Button) findViewById(R.id.btn_Clear);
        mBackButton = (Button) findViewById(R.id.btn_back);

        // Radio
        rgEnvironment = (RadioGroup) findViewById(R.id.rg_environment);
        rbEnvProd = (RadioButton) findViewById(R.id.rb_env_prod);
        rbEnvTest = (RadioButton) findViewById(R.id.rb_env_test);

        mPublicKey = (EditText) findViewById(R.id.et_androidpay_public_key);

        // Add public key from config.properties
        mPublicKey.setText(mStrPublicKey);

        mUse.setOnClickListener(mClickListener);
        mClear.setOnClickListener(mClickListener);
        mBackButton.setOnClickListener(mClickListener);

    } // end of init()

    /**
     * This method is called when button click listener
     */
    private final View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    final Intent intent = new Intent(PublicKeyActivity.this,Menu.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_Use:
                    setValuesIntoShardPrf();
                    buttonUseClick();
                    break;
                case R.id.btn_Clear:
                    buttonClearClick();
                    break;
                default:
                    break;
            }
        }
    }; // end of onClickListener

    /**
     * This method is used to execute async task
     */
    private void buttonUseClick(){
        mStrPublicKey = mPublicKey.getText().toString();

        RadioButton selectRadio = (RadioButton) findViewById(rgEnvironment.getCheckedRadioButtonId());
        mStrEnvironment = selectRadio.getText().toString();

        Intent intentUse = new Intent(PublicKeyActivity.this, AndroidPayActivity.class);
        intentUse.putExtra("AndroidPayPublicKey", mStrPublicKey);
        intentUse.putExtra("AndroidPayEnvironment", mStrEnvironment);

        startActivity(intentUse);
        finish();

    } // end of buttonUseClick()

    /**
     * This method is used to clear edit text
     */
    private void buttonClearClick(){

        mPublicKey.setText("");
    } // end of buttonClearClick()

} // end of class PublicKeyActivity
