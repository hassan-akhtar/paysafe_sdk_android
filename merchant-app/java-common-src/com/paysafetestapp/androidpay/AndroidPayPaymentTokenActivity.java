package com.paysafetestapp.androidpay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paysafe.Environment;
import com.paysafe.PaysafeApiClient;
import com.paysafe.common.Error;
import com.paysafe.common.PaysafeException;
import com.paysafe.customervault.AndroidPayPaymentToken;
import com.paysafe.customervault.Card;
import com.paysafe.customervault.SingleUseToken;
import com.paysafetestapp.Menu;
import com.paysafetestapp.PaysafeApplication;
import com.paysafetestapp.R;
import com.paysafetestapp.authorize.Authorize;
import com.paysafetestapp.utils.Constants;
import com.paysafetestapp.utils.Utils;

import java.io.IOException;


/**
 * Created by asawari.vaidya on 10-04-2017.
 */

public class AndroidPayPaymentTokenActivity extends Activity {

    // Button
    private Button mBackButton;
    private Button mOKButton;

    // EditText
    private EditText mAndroidPayPaymentBundleEditText;
    private EditText mEncryptedMessageEditText;
    private EditText mEphemeralPublicKeyEditText;
    private EditText mTagEditText;

    // Context
    private Context mContext;

    //String
    private String mAndroidPayPaymentBundle;
    private String mEncryptedMessage;
    private String mEphemeralPublicKey;
    private String mTag;

    // Response String
    private String mPaymentToken = null;
    private Integer mTimeToLiveSeconds = null;

    private AndroidPayPaymentToken androidPayPaymentToken = null;
    private String ephemeralMessage = null;
    private String tag = null;

    private Card card = null;
    private String lastDigits = null;
    private String status = null;
    private Error error = null;

    // Error
    private String mMessage;
    private String mCode;

    private PaysafeApiClient client;

    // Configuration
    private String merchantApiKeySBOX;
    private String merchantApiPasswordSBOX;
    private String merchantAccountNumberSBOX;

    /**
     * On Create Activity.
     * @param savedInstanceState Object of Bundle holding instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.androidpaypaymenttoken);

        init();
    } // end of onCreate()

    /**
     * This method is called when initialize UI
     */
    private void init() {
        mContext = this;
        // Button
        mBackButton = (Button) findViewById(R.id.btn_back);
        mOKButton = (Button) findViewById(R.id.btn_Confirm);
        mBackButton.setOnClickListener(mClickListener);
        mOKButton.setOnClickListener(mClickListener);
        // Edit Text
        mAndroidPayPaymentBundleEditText = (EditText) findViewById(R.id.et_android_pay_payment_token_bundle);
        mEncryptedMessageEditText = (EditText) findViewById(R.id.et_encrypted_message);
        mEphemeralPublicKeyEditText = (EditText) findViewById(R.id.et_ephemeral_public_key);
        mTagEditText = (EditText) findViewById(R.id.et_tag);

        // Get data from Intent
        Intent intentAndroidPay = getIntent();

        mAndroidPayPaymentBundleEditText.setText(
                intentAndroidPay.getStringExtra("AndroidPayPaymentBundle"));
        mEncryptedMessageEditText.setText(intentAndroidPay.getStringExtra("EncryptedMessage"));
        mEphemeralPublicKeyEditText.setText(intentAndroidPay.getStringExtra("EphemeralPublicKey"));
        mTagEditText.setText(intentAndroidPay.getStringExtra("Tag"));

    } // end of init()

    /**
     * This method is called when button click listener
     */
    private final View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    final Intent intent = new Intent(AndroidPayPaymentTokenActivity.this,Menu.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_Confirm:
                    buttonConfirmClick();
                    break;

                default:
                    break;
            }
        }
    }; // end of onClickListener

    /**
     * This method is used to execute async task
     */
    private void buttonConfirmClick(){
        getValuesfromEditText();
        //Check Internet connection
        if (!isCheckInternet()) {
            Toast.makeText(getApplicationContext(),	Constants.PLEASE_TURN_ON_YOUR_INTERNET, Toast.LENGTH_LONG).show();
            return;
        }
        //Execute AsyncTask for SingleUseToken
        new SingleUseTokenTask().execute();
    } // end of buttonConfirmClick()


    /**
     * Single Use Token Task.
     * This class is used to create the single use token.
     */
    private class SingleUseTokenTask extends AsyncTask<String, Void, SingleUseToken> {

        /**
         * On Pre Execute.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.startProgressDialog(AndroidPayPaymentTokenActivity.this,
                    getString(R.string.loading_text));
        } // end of onPreExecute()

        /**
         * On Post Execute.
         * @param singleUseTokenObject
         */
        @Override
        protected void onPostExecute(SingleUseToken singleUseTokenObject) {
            super.onPostExecute(singleUseTokenObject);
            try {
                Utils.stopProgressDialog();

                if(mMessage != null || mCode != null){
                    Utils.showDialogAlert(Constants.ERROR_CODE+mCode+"\n"+Constants.ERROR_MESSAGE+mMessage, mContext);
                }else if(singleUseTokenObject != null){
                    // android pay payment token
                    androidPayPaymentToken = singleUseTokenObject
                            .getAndroidPayPaymentToken();
                    mPaymentToken = singleUseTokenObject.getPaymentToken();
                    mTimeToLiveSeconds = singleUseTokenObject.getTimeToLiveSeconds();
                    ephemeralMessage = androidPayPaymentToken.getEphemeralPublicKey();
                    tag = androidPayPaymentToken.getTag();
                    // card
                    card = singleUseTokenObject.getCard();
                    lastDigits = card.getLastDigits();
                    status = card.getStatus();

                    Utils.debugLog("AndroidPay Payment Token Response: " + androidPayPaymentToken + "\n\n" +
                            "Ephemeral Public Key: " + ephemeralMessage + "\n\n" +
                            "Tag: " + tag + "\n\n" +
                            "Card Last Digits: " + lastDigits + "\n\n" +
                            "Card  Status: " + status);

                    //error
                    error = singleUseTokenObject.getError();

                    String connectivityError = singleUseTokenObject.getConnectivityError();
                    if (!Utils.isEmpty(connectivityError)) {
                        Utils.showDialogAlert(Constants.PLEASE_TURN_ON_YOUR_INTERNET,mContext);
                    }
                    if(error != null ){
                        String strMessage = error.getMessage();
                        String strCode = error.getCode();
                        Utils.showDialogAlert(strCode + ": "+ strMessage, mContext);
                    }
                    else if (!Utils.isEmpty(mPaymentToken)) {
                        if (!Utils.isEmpty(lastDigits)) {
                            showDialogAlert("Payment Token :" + "  " + mPaymentToken + "\n" +
                                    "Payment Token :" + "  " + mPaymentToken + "\n" +
                                    "Time To Live Seconds :" + "  " + mTimeToLiveSeconds + "\n" +
                                    "Card Last Digit :"+" "+ lastDigits + "\n" +
                                    "Card Status :"+" "+ status + "\n"+
                                    "AndroidPay - Ephemeral Public Key : " + ephemeralMessage + "\n" +
                                    "Android Pay - Tag : " + tag, mContext);
                        } else {
                            showDialogAlert("Android Payment Token :" + "  " + mPaymentToken,	mContext);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } // end of onPostExecute()

        /**
         * Do In Background.
         * @param args
         * @return Single Use Token Object.
         */
        @Override
        protected SingleUseToken doInBackground(String... args) {
            return singleUseTokenRequest();

        } // end of doInBackground()
    } // end of class SingleUseTokenTask

    /**
     * This method will make a call to Single Use Token  API.
     *
     * @return Single Use Token Object.
     */
    private SingleUseToken singleUseTokenRequest(){

        try {
            merchantApiKeySBOX = Utils.getProperty("merchant_api_key_sbox", mContext);
            merchantApiPasswordSBOX = Utils.getProperty("merchant_api_password_sbox", mContext);
            merchantAccountNumberSBOX = Utils.getProperty("merchant_account_number_sbox", mContext);

        } catch(IOException ioExp) {
            Utils.showDialogAlert("IOException: "+ ioExp.getMessage(), mContext);
        }

        // Client Object
        client = new PaysafeApiClient(merchantApiKeySBOX, merchantApiPasswordSBOX,
                                    Environment.TEST, merchantAccountNumberSBOX);

        // Retrieve values from Edit Text to process the single use token object.
        getValuesfromEditText();

        try {
            SingleUseToken sObjResponse;

            // Make API call for single use token
            sObjResponse = client.customerVaultService()
                    .createAndroidPayPaymentToken(
                            SingleUseToken.builder()
                                    .androidPayPaymentToken()
                                    .encryptedMessage(mEncryptedMessage)
                                    .ephemeralPublicKey(mEphemeralPublicKey)
                                    .tag(mTag)
                                    .done()
                                    .build());

            return sObjResponse;
        } catch (PaysafeException e) {

            mMessage = e.getMessage();
            mCode = e.getCode();

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    } // end of singleUseTokenRequest()

    /**
     * This method is used to get values from edit text
     */
    private void getValuesfromEditText() {
        mAndroidPayPaymentBundle = mAndroidPayPaymentBundleEditText.getText().toString();
        mEncryptedMessage = mEncryptedMessageEditText.getText().toString();
        mEphemeralPublicKey = mEphemeralPublicKeyEditText.getText().toString();
        mTag = mTagEditText.getText().toString();

    } // end of getValuesfromEditText()

    /**
     * This method is called when check the Internet
     *
     * @return Boolean value for checking whether Internet setting is ON/OFF.
     *
     */
    private boolean isCheckInternet() {
        boolean isNetworkAvailable = Utils.isNetworkAvailable(PaysafeApplication.mApplicationContext);
        boolean isOnline = Utils.isOnline(PaysafeApplication.mApplicationContext);
        if (isNetworkAvailable) {
            return true;
        } else if (isOnline) {
            return true;
        }
        return false;
    } // end of isCheckInternet()

    /**
     * This method is called when back pressed finished the activity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentCheckOut = new Intent(AndroidPayPaymentTokenActivity.this,
                PublicKeyActivity.class);
        startActivity(intentCheckOut);
        finish();
    } // end of onBackPressed()

    /**
     * This method is called show alert dialog
     *
     * @param alertMessage Alert message for the Dialog Box.
     * @param context Context object.
     */
    private void showDialogAlert(String alertMessage, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(alertMessage);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                 new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentPaymentToken = new Intent(AndroidPayPaymentTokenActivity.this,
                                Authorize.class);
                        intentPaymentToken.putExtra("AndroidPayPaymentToken", mPaymentToken);
                        startActivity(intentPaymentToken);
                        finish();
                    }
                });
        alertDialog.show();
    } // end of showDialogAlert()

    /**
     * On Destroy.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(client != null){
            client = null;
        }
    } // end of onDestroy()

} // end of class AndroidPayPaymentToken
