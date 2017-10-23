package com.paysafetestapp.paywithgoogle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.gson.Gson;
import com.paysafe.customervault.PayWithGooglePaymentToken;
import com.paysafetestapp.CardDetails;
import com.paysafetestapp.Checkout;
import com.paysafetestapp.PaysafeApplication;
import com.paysafetestapp.R;
import com.paysafetestapp.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by asawari.vaidya on 25-09-2017.
 */

public class PayWithGoogleActivity extends AppCompatActivity {

    // Context
    private Context mContext;

    // Button
    private Button mBtnPayWithGoogle;
    private Button mBtnSingleUseToken;
    private Button mBackButton;

    private PayWithGooglePaymentToken payWithGooglePayload;
    private PaymentsClient paymentsClient;

    private String mMerchantUsername;

    private String mPwgPayload;
    private String mSignature;
    private String mProtocolVersion;
    private String mSignedMessage;

    // Flags to set/unset payment options
    private int flagPayWithGoogle;
    private int flagAndroidPay;

    // Flag array
    private ArrayList<Integer> flagPaymentMethods;

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 888;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paywithgoogle);

        // Context
        mContext = this;

        init();
        setMerchantUsername();

        // Setup instance for PaymentsClient
        paymentsClient = Wallet.getPaymentsClient(this,
                new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        //.setEnvironment(WalletConstants.ENVIRONMENT_PRODUCTION)
                        .build());

        // Check if Google Payment API is supported on the device
        isReadyToPay();
    } // end of onCreate()

    /**
     * This method is called when initialize UI.
     */
    private void init() {

        // Button
        mBtnPayWithGoogle = (Button) findViewById(R.id.btn_confirm_pay_with_google);
        mBtnSingleUseToken = (Button) findViewById(R.id.btn_single_use_token);
        mBackButton = (Button) findViewById(R.id.btn_back);

        // Button OnClick Listener
        mBtnPayWithGoogle.setOnClickListener(mClickListener);
        mBtnSingleUseToken.setOnClickListener(mClickListener);
        mBackButton.setOnClickListener(mClickListener);

    } // end of init()

    /**
     * Set Merchant Username to use it in Payment Data Request
     */
    private void setMerchantUsername() {
        try {
            mMerchantUsername = Utils.getProperty("merchant_api_key_id_sbox", mContext);
        } catch(IOException ioExp) {
            Utils.showDialogAlert("IOException: "+ ioExp.getMessage(), mContext);
        }
    } // end of setMerchantUsername()

    /**
     * This method is called when button click listener.
     */
    private final View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm_pay_with_google:
                    requestPayment(v);
                    break;

                case R.id.btn_single_use_token:
                    Intent intentBack = new Intent(PayWithGoogleActivity.this,CardDetails.class);
                    startActivity(intentBack);
                    finish();
                    break;
                case R.id.btn_back:
                    final Intent intent = new Intent(PayWithGoogleActivity.this,Checkout.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }; // end of OnCLickListener

    /**
     * IsReadyToPay
     */
    private void isReadyToPay() {
        IsReadyToPayRequest request = IsReadyToPayRequest.newBuilder()
                .addAllowedPaymentMethod( WalletConstants.PAYMENT_METHOD_CARD)
                .addAllowedPaymentMethod( WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                .build();
        Task <Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    public void onComplete( Task<Boolean> task) {
                        try {
                            boolean result =
                                    task.getResult( ApiException.class);
                            if(result == true) {
                                // Google Payment supported on device
                                //show Google as payment option
                                flagPaymentMethods = new ArrayList<Integer>();
                                // CHECK, if device supports NFC
                                // if YES, show below payment methods
                                // 1. Card on file
                                // 2. Android Pay
                                if (Utils.isNFCAvailable(PaysafeApplication.mApplicationContext)) {
                                    // set payment methods
                                    flagPayWithGoogle = WalletConstants.PAYMENT_METHOD_CARD;
                                    flagAndroidPay = WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD;
                                    // Add Options to Payment Methods
                                    flagPaymentMethods.add(flagPayWithGoogle);
                                    flagPaymentMethods.add(flagAndroidPay);

                                }
                                else {
                                    // if NO (device does'nt support NFC), show below payment methods
                                    // 1. Card on file
                                    flagPayWithGoogle = WalletConstants.PAYMENT_METHOD_CARD;
                                    // Add Options to Payment Methods
                                    flagPaymentMethods.add(flagPayWithGoogle);

                                }

                            } else {
                                // Google Payment not supported on device
                                //hide Google as payment option
                                Utils.showDialogAlert("Google Payment Option Not Supported!",
                                        mContext);
                                mBtnPayWithGoogle.setEnabled(false);

                            }
                        } catch (ApiException exception) {
                        }
                    }
                });
    } // end of isReadyToPay()

    /**
     * CreatePaymentDataRequest
     */
    private PaymentDataRequest createPaymentDataRequest() {
        PaymentDataRequest .Builder request =
                PaymentDataRequest .newBuilder()
                        .setTransactionInfo(
                                TransactionInfo.newBuilder()
                                        .setTotalPriceStatus(
                                                WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                                        .setTotalPrice("1.00")
                                        .setCurrencyCode("USD")
                                        .build())
                        .addAllowedPaymentMethods(flagPaymentMethods)
                        .setCardRequirements(
                                CardRequirements.newBuilder()
                                        .addAllowedCardNetworks(Arrays.asList(
                                                WalletConstants.CARD_NETWORK_AMEX,
                                                WalletConstants.CARD_NETWORK_DISCOVER,
                                                WalletConstants.CARD_NETWORK_VISA,
                                                WalletConstants.CARD_NETWORK_MASTERCARD))
                                        .build());
        PaymentMethodTokenizationParameters params =
                PaymentMethodTokenizationParameters .newBuilder()
                        .setPaymentMethodTokenizationType(
                                WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY )
                        .addParameter("gateway", getResources().getString(R.string.gateway))
                        .addParameter("gatewayMerchantId",
                         mMerchantUsername)
                        .build();
        request.setPaymentMethodTokenizationParameters(params);
        return request.build();
    } // end of createPaymentDataRequest()

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData =
                                PaymentData.getFromIntent(data);
                        String token = paymentData.getPaymentMethodToken().getToken();

                        // Convert from JSON format
                        Gson gson = new Gson();
                        payWithGooglePayload = gson.fromJson(token,
                                PayWithGooglePaymentToken.class);

                        // Get data
                        mPwgPayload = token;
                        mSignature = payWithGooglePayload.getSignature();
                        mProtocolVersion = payWithGooglePayload.getProtocolVersion();
                        mSignedMessage = payWithGooglePayload.getSignedMessage();

                        // Call Next Activity using Intent
                        Intent intentPayWithGoogle = new Intent(PayWithGoogleActivity.this,
                                PwgPayloadActivity.class);
                        intentPayWithGoogle.putExtra("PwgPayload", mPwgPayload);
                        intentPayWithGoogle.putExtra("Signature", mSignature);
                        intentPayWithGoogle.putExtra("ProtocolVersion", mProtocolVersion);
                        intentPayWithGoogle.putExtra("SignedMessage", mSignedMessage);
                        startActivity(intentPayWithGoogle);
                        finish();

                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    case AutoResolveHelper .RESULT_ERROR:
                        Status status =
                                AutoResolveHelper
                                        .getStatusFromIntent(data);

                        // Log the status for debugging
                        // Generally there is no need to show an error to
                        // the user as the Google Payment API will do that
                        // LOG
                        Utils.debugLog("Status Code: " + status.getStatusCode());
                        Utils.debugLog("Status message: " + status.getStatusMessage());
                        Utils.debugLog("Status : " + status.getStatus().toString());
                        break;
                    default:
                        // Do nothing.
                }
                break;
            default:
                // Do nothing.
        }
    } // end of onActivityResult()

    /**
     * Request Payment
     * @param view
     */
    private void requestPayment(View view) {
        PaymentDataRequest request = createPaymentDataRequest ();
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request), this,
                    LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    } // end of requestPayment()

    /**
     * This method is called when back pressed finished the activity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    } // end of onBackPressed()

} // end of class PayWith Google