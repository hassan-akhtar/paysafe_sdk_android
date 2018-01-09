package com.paysafetestapp.googlepay;

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

import com.google.gson.Gson;
import com.paysafe.Environment;
import com.paysafe.PaysafeApiClient;
import com.paysafe.common.Error;
import com.paysafe.common.PaysafeException;
import com.paysafe.customervault.Card;
import com.paysafe.customervault.GooglePayToken;
import com.paysafe.customervault.SingleUseToken;
import com.paysafetestapp.Checkout;
import com.paysafetestapp.PaysafeApplication;
import com.paysafetestapp.R;
import com.paysafetestapp.authorize.Authorize;
import com.paysafetestapp.utils.Constants;
import com.paysafetestapp.utils.Utils;

import java.io.IOException;


/**
 * Created by asawari.vaidya on 10-04-2017.
 */

public class GooglePayPayloadActivity extends Activity {

    // Button
    private Button mBackButton;
    private Button mOKButton;

    // EditText
    private EditText mGooglePayPayloadEditText;
    private EditText mSignatureEditText;
    private EditText mProtocolVersionEditText;
    private EditText mSignedMessageEditText;

    // Context
    private Context mContext;

    //String
    private String mSignature;
    private String mProtocolVersion;
    private String mSignedMessage;

    // Response String
    private String mPaymentToken = null;
    private String mPaymentMethod = null;
    private Integer mTimeToLiveSeconds = null;

    private GooglePayToken googlePayToken = null;

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
        setContentView(R.layout.googlepaypayload);

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
        mGooglePayPayloadEditText = (EditText) findViewById(R.id.pwg_payload);
        mSignatureEditText = (EditText) findViewById(R.id.et_signature);
        mProtocolVersionEditText = (EditText) findViewById(R.id.et_protocol_version);
        mSignedMessageEditText = (EditText) findViewById(R.id.et_signed_message);

        // Get data from Intent
        Intent intentGooglePay = getIntent();

        mGooglePayPayloadEditText.setText(intentGooglePay.getStringExtra("PwgPayload"));
        mSignatureEditText.setText(intentGooglePay.getStringExtra("Signature"));
        mProtocolVersionEditText.setText(intentGooglePay.getStringExtra("ProtocolVersion"));
        mSignedMessageEditText.setText(intentGooglePay.getStringExtra("SignedMessage"));

    } // end of init()

    /**
     * This method is called when button click listener
     */
    private final View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    final Intent intent = new Intent(GooglePayPayloadActivity.this,Checkout.class);
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
        getValuesFromEditText();
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
            Utils.startProgressDialog(GooglePayPayloadActivity.this,
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
                    // pay with google payment token
                    googlePayToken = singleUseTokenObject.getGooglePayToken();
                    mPaymentToken = singleUseTokenObject.getPaymentToken();
                    mPaymentMethod = singleUseTokenObject.getGooglePayToken()
                            .getPaymentMethod();
                    mTimeToLiveSeconds = singleUseTokenObject.getTimeToLiveSeconds();
                    // card
                    card = singleUseTokenObject.getCard();
                    lastDigits = card.getLastDigits();
                    status = card.getStatus();

                    Utils.debugLog("Google Pay Token Response: " + googlePayToken + "\n\n"
                            + "Card Last Digits: " + lastDigits + "\n\n"
                            + "Card  Status: " + status);

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
                                    "Time To Live Seconds :" + "  " + mTimeToLiveSeconds + "\n" +
                                    "Card Last Digit :"+" "+ lastDigits + "\n" +
                                    "Card Status :"+" "+ status, mContext);
                        } else {
                            showDialogAlert("Google Pay Token :" + "  " + mPaymentToken,	mContext);
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
        getValuesFromEditText();

        try {
            SingleUseToken sObjResponse;

            // Make API call for single use token

            sObjResponse = client.customerVaultService()
                    .createGooglePayToken(
                            SingleUseToken.builder()
                                    .googlePayToken()
                                        .signature(mSignature)
                                        .protocolVersion(mProtocolVersion)
                                        .signedMessage(mSignedMessage)
                                        .done()
                                    .build());

            Gson gson = new Gson();
            String requestData = gson.toJson(sObjResponse);

            // LOG
            Utils.debugLog("GooglePayPayloadActivity-Response "+ requestData);

            return sObjResponse;
        } catch (PaysafeException e) {

            mMessage = e.getMessage();
            mCode = e.getCode();

            // LOG
            Utils.debugLog("GooglePayPayloadActivity-Error Code: "+ mCode);
            Utils.debugLog("GooglePayPayloadActivity-Error Message: "+ mMessage);

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    } // end of singleUseTokenRequest()

    /**
     * This method is used to get values from edit text
     */
    private void getValuesFromEditText() {
        mSignature = mSignatureEditText.getText().toString();
        mProtocolVersion = mProtocolVersionEditText.getText().toString();
        mSignedMessage = mSignedMessageEditText.getText().toString();

    } // end of getValuesFromEditText()

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
        Intent intentCheckOut = new Intent(GooglePayPayloadActivity.this,
                GooglePayActivity.class);
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

                        Intent intentPaymentToken = new Intent(GooglePayPayloadActivity.this,
                                Authorize.class);

                        intentPaymentToken.putExtra("PwgPaymentToken", mPaymentToken);
                        intentPaymentToken.putExtra("PwgPaymentMethod", mPaymentMethod);
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

} // end of class GooglePayPayloadActivity
