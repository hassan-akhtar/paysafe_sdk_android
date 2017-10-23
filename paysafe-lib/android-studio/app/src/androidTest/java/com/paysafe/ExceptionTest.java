package com.paysafe;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.paysafe.utils.Constants;
import com.paysafe.common.PaysafeException;
import com.paysafe.customervault.SingleUseToken;
import com.paysafe.utils.Utils;

import junit.framework.Assert;

import java.io.IOException;

/**
 * Created by Asawari.Vaidya on 23-09-2015.
 */
public class ExceptionTest extends ApplicationTestCase<Application> {

    // Paysafe Api Client Object
    private static PaysafeApiClient client;

    // Credentials
    private String merchantApiKey;
    private String merchantApiPassword;

    // Merchant Account Number
    private String merchantAccountNumber;

    public ExceptionTest() {
        super(Application.class);
        getConfigurationProperties();
    }

    private void getConfigurationProperties() {
        try {
            merchantApiKey = Utils.getProperty("merchant-api-key-id", this.getContext());
            merchantApiPassword = Utils.getProperty("merchant-api-key-password", this.getContext());
            merchantAccountNumber = Utils.getProperty("merchant-account-number", this.getContext());
        } catch(IOException ioExp) {
            Utils.debugLog("EXCEPTION TEST: IOException: "+ ioExp.getMessage());
        }
    } // end of getConfigurationProperties()

    /**
     * Test Case to Create Single Use Token with Missing or Invalid Merchant Account Number.
     * @throws Exception
     */
    public void testMissingOrInvalidAccount() throws Exception {

        client = new PaysafeApiClient(merchantApiKey, merchantApiPassword, Environment.TEST);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
                            .cardExpiry()
                            .month(7)
                            .year(2019)
                            .done()
                            .billingAddress()
                            .street("100 Queen Street West")
                            .street2("Unit 201")
                            .city("Toronto")
                            .country("CA")
                            .state("ON")
                            .zip("M5H 2N2")
                            .done()
                            .done()
                            .build());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("EXCEPTION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            Utils.debugLog("EXCEPTION TEST: Missing or Invalid Account: " + oExp.getMessage());
                    Assert.assertNotNull("Missing or Invalid Account.", oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("EXCEPTION TEST: " + e.getMessage());
        }
    } // end of testMissingOrInvalidAccount

    /**
     * Test Case to Create Single Use Token with Invalid Authentication Credentials.
     * @throws Exception
     */
    public void testInvalidAuthenticationCredentials() throws Exception {
        client = new PaysafeApiClient("username", "password", Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
                            .cardExpiry()
                            .month(7)
                            .year(2019)
                            .done()
                            .billingAddress()
                            .street("100 Queen Street West")
                            .street2("Unit 201")
                            .city("Toronto")
                            .country("CA")
                            .state("ON")
                            .zip("M5H 2N2")
                            .done()
                            .done()
                            .build());

            Assert.assertEquals("5279", sObjResponse.getError().getCode());
            Assert.assertEquals("The authentication credentials are invalid.",
                    sObjResponse.getError().getMessage());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("EXCEPTION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("EXCEPTION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("EXCEPTION TEST: " + e.getMessage());
        }
    } // end of testInvalidAuthenticationCredentials()
}
