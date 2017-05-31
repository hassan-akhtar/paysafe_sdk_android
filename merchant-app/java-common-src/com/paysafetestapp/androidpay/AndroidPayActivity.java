package com.paysafetestapp.androidpay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;
import com.google.gson.Gson;
import com.paysafe.customervault.AndroidPayPaymentToken;
import com.paysafetestapp.Menu;
import com.paysafetestapp.R;
import com.paysafetestapp.utils.Constants;
import com.paysafetestapp.utils.Utils;

/**
 * Created by asawari.vaidya on 13-04-2017.
 */

public class AndroidPayActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Context
    private Context mContext;

    private static int ENV;
    private String mStrPublicKey;
    private String mStrEnvironment;
    private String mAndroidPayPaymentBundle;
    private String mEncryptedMessage;
    private String mEphemeralKey;
    private String mTag;

    // Button
    private Button mConfirm;

    private ProgressDialog mProgressDialog;

    private SupportWalletFragment mWalletFragment;
    public static final int MASKED_WALLET_REQUEST_CODE = 888;

    public static final String WALLET_FRAGMENT_ID = "wallet_fragment";

    private MaskedWallet mMaskedWallet;
    private GoogleApiClient mGoogleApiClient;

    public static final int FULL_WALLET_REQUEST_CODE = 889;
    private FullWallet mFullWallet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.androidpay);

        // Context
        mContext = this;

        // Button
        mConfirm = (Button) findViewById(R.id.btn_confirm_android_pay);

        // Get Android Pay Public Key from PublicKeyActivity
        Intent intentPublicKey = getIntent();
        mStrPublicKey = intentPublicKey.getStringExtra("AndroidPayPublicKey");
        mStrEnvironment = intentPublicKey.getStringExtra("AndroidPayEnvironment");

        Utils.debugLog("selectRadio: " + mStrEnvironment);
        if (mStrEnvironment != null && mStrEnvironment.equals("PROD")){
            ENV = WalletConstants.ENVIRONMENT_PRODUCTION;
            Utils.debugLog("ENVIRONMENT_PRODUCTION");
        } else if (mStrEnvironment != null && mStrEnvironment.equals("TEST")) {
            ENV = WalletConstants.ENVIRONMENT_TEST;
            Utils.debugLog("ENVIRONMENT_TEST");
        }

        // Initialize GoogleApiClient Object
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(ENV)
                        .setTheme(WalletConstants.THEME_LIGHT)
                        .build())
                .build();

        // Check if user is ready to use Android Pay
        // [START is_ready_to_pay]
        showProgressDialog();
        Wallet.Payments.isReadyToPay(mGoogleApiClient).setResultCallback(
                new ResultCallback<BooleanResult>() {
                    @Override
                    public void onResult(@NonNull BooleanResult booleanResult) {
                        hideProgressDialog();

                        if (booleanResult.getStatus().isSuccess()) {
                            if (booleanResult.getValue()) {
                                // Show Android Pay buttons and hide regular checkout button
                                // [START_EXCLUDE]
                                Utils.debugLog("isReadyToPay:true");

                                createAndAddWalletFragment();
                            } else {
                                // Hide Android Pay buttons, show a message that Android Pay
                                // cannot be used yet, and display a traditional checkout button
                                // [START_EXCLUDE]
                                findViewById(R.id.wallet_button_holder)
                                        .setVisibility(View.GONE);
                                Toast.makeText(mContext, Constants.NO_NFC_SUPPORT,
                                        Toast.LENGTH_LONG).show();
                                Utils.debugLog("isReadyToPay:false:" + booleanResult.getStatus());
                                                            }
                        } else {
                            // Error making isReadyToPay call
                            Utils.debugLog("isReadyToPay:" + booleanResult.getStatus());
                        }
                    }
                });
        // [END is_ready_to_pay]
    }

    private void createAndAddWalletFragment() {

        // Check if WalletFragment exists
        mWalletFragment = (SupportWalletFragment) getSupportFragmentManager()
                .findFragmentByTag(WALLET_FRAGMENT_ID);

        // It does not, add it
        if(mWalletFragment == null) {
            // Wallet fragment style
            WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                    .setBuyButtonText(WalletFragmentStyle.BuyButtonText.BUY_WITH)
                    .setMaskedWalletDetailsButtonBackgroundColor(WalletFragmentStyle.BuyButtonAppearance.ANDROID_PAY_LIGHT_WITH_BORDER)
                    .setBuyButtonWidth(WalletFragmentStyle.Dimension.MATCH_PARENT);
            // Wallet fragment options
            WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                    .setEnvironment(ENV)
                    .setFragmentStyle(walletFragmentStyle)
                    .setTheme(WalletConstants.THEME_LIGHT)
                    .setMode(WalletFragmentMode.BUY_BUTTON)
                    .build();

            // Initialize the WalletFragment
            WalletFragmentInitParams.Builder startParamsBuilder =
                    WalletFragmentInitParams.newBuilder()
                            .setMaskedWalletRequest(generateMaskedWalletRequest())
                            .setMaskedWalletRequestCode(MASKED_WALLET_REQUEST_CODE)
                            .setAccountName("Google I/O Codelab");

            // Instantiate the WalletFragment
            mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
            mWalletFragment.initialize(startParamsBuilder.build());

            // Add the fragment to the UI
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.wallet_button_holder, mWalletFragment, WALLET_FRAGMENT_ID)
                    .commit();
        }
    } // end of createAndAddWalletFragment()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case MASKED_WALLET_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mMaskedWallet =  data
                                .getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                        Utils.debugLog("Got Masked Wallet");
                        if (mMaskedWallet != null)
                            mConfirm.setVisibility(View.VISIBLE);
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user canceled the operation
                        break;
                    case WalletConstants.RESULT_ERROR:
                        Utils.debugLog("An Error Occurred");
                        break;
                }
                break;
            case FULL_WALLET_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mFullWallet = data
                                .getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                        PaymentMethodToken pmtdToken = mFullWallet.getPaymentMethodToken();
                        String trxId = mFullWallet.getGoogleTransactionId();
                        String token = pmtdToken.getToken();

                        // Update transaction status
                        Wallet.Payments.notifyTransactionStatus(mGoogleApiClient,
                                generateNotifyTransactionStatusRequest(
                                        trxId,
                                        NotifyTransactionStatusRequest.Status.SUCCESS));

                        // Show the credit card number
                        Utils.debugLog(
                                "Got Token:" + token + ", trxid=" + trxId
                                );

                        Gson gson = new Gson();
                        AndroidPayPaymentToken objAndroidPayPaymentToken =
                                gson.fromJson(token, AndroidPayPaymentToken.class);

                        mAndroidPayPaymentBundle = token;
                        mEncryptedMessage = objAndroidPayPaymentToken.getEncryptedMessage();
                        mEphemeralKey = objAndroidPayPaymentToken.getEphemeralPublicKey();
                        mTag = objAndroidPayPaymentToken.getTag();

                        // Intent: send parameters to AndroidPayPaymentTokenActivity.java
                        Intent intentAndroidPay = new Intent(AndroidPayActivity.this,
                                AndroidPayPaymentTokenActivity.class);
                        intentAndroidPay.putExtra("AndroidPayPaymentBundle", mAndroidPayPaymentBundle);
                        intentAndroidPay.putExtra("EncryptedMessage", mEncryptedMessage);
                        intentAndroidPay.putExtra("EphemeralPublicKey", mEphemeralKey);
                        intentAndroidPay.putExtra("Tag", mTag);
                        startActivity(intentAndroidPay);
                        finish();
                        break;
                    case WalletConstants.RESULT_ERROR:
                        Utils.debugLog("An Error Occurred");
                        break;
                }
                break;
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        Utils.debugLog("Google Api Client connected");
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        Utils.debugLog("Googlle Api Client connection stopped");
    }

    @Override
    public void onConnected(Bundle bundle) {
        // GoogleApiClient is connected, we don't need to do anything here
        if(mGoogleApiClient.isConnected()){
            Utils.debugLog("Google_Api_Client: It was connected on (onConnected) function, " +
                    "working as it should.");
        }
        else{
            Utils.debugLog("Google_Api_Client: It was NOT connected on (onConnected) " +
                    "function, It is definetly bugged.");
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // GoogleApiClient is temporarily disconnected, no action needed
        Utils.debugLog("Google Api Client connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // GoogleApiClient failed to connect, we should log the error and retry
        Utils.debugLog("Google Api Client connection failed");
    }

    private MaskedWalletRequest generateMaskedWalletRequest() {

        // This is just an example publicKey for the purpose of this codelab.
        // To learn how to generate your own visit:
        // https://github.com/android-pay/androidpay-quickstart
        PaymentMethodTokenizationParameters parameters =
                PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(
                                PaymentMethodTokenizationType.NETWORK_TOKEN)
                        .addParameter("publicKey", mStrPublicKey)
                        .build();

        MaskedWalletRequest maskedWalletRequest =
                MaskedWalletRequest.newBuilder()
                        .setMerchantName("Google I/O Codelab")
                        .setPhoneNumberRequired(true)
                        .setShippingAddressRequired(true)
                        .setCurrencyCode("USD")
                        .setCart(Cart.newBuilder()
                                .setCurrencyCode("USD")
                                .setTotalPrice("0.01")
                                .addLineItem(LineItem.newBuilder()
                                        .setCurrencyCode("USD")
                                        .setDescription("Google I/O Sticker")
                                        .setQuantity("1")
                                        .setUnitPrice("0.01")
                                        .setTotalPrice("0.01")
                                        .build())
                                .build())
                        .setEstimatedTotalPrice("0.01")
                        .setPaymentMethodTokenizationParameters(parameters)
                        .build();
        return maskedWalletRequest;
    }

    private FullWalletRequest generateFullWalletRequest(String googleTransactionId) {
        FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setCart(Cart.newBuilder()
                        .setCurrencyCode("USD")
                        .setTotalPrice("10.10")
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("Google I/O Sticker")
                                .setQuantity("1")
                                .setUnitPrice("0.01")
                                .setTotalPrice("0.01")
                                .build())
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("Tax")
                                .setRole(LineItem.Role.TAX)
                                .setTotalPrice("0.01")
                                .build())
                        .build())
                .build();
        return fullWalletRequest;
    }

    public void requestFullWallet(View view) {
        if (mMaskedWallet == null) {
            Utils.debugLog("No masked wallet, can't confirm");
            return;
        }
        Wallet.Payments.loadFullWallet(mGoogleApiClient,
                generateFullWalletRequest(mMaskedWallet.getGoogleTransactionId()),
                FULL_WALLET_REQUEST_CODE);

        Utils.debugLog("End of method requestFullWallet()");
    }

    public void backToMenu(View view) {
        final Intent intent = new Intent(AndroidPayActivity.this,Menu.class);
        startActivity(intent);
        finish();
    }

    public static NotifyTransactionStatusRequest generateNotifyTransactionStatusRequest(
            String googleTransactionId, int status) {
        return NotifyTransactionStatusRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setStatus(status)
                .build();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

} // end of class AndroidPayActivity
