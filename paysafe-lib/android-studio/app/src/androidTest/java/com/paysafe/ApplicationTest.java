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
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    // Paysafe Api Client Object
    private static PaysafeApiClient client;

    // Credentials
    private String merchantApiKeyId;
    private String merchantApiKeyPassword;

    // Merchant Account Number
    private String merchantAccountNumber;

    public ApplicationTest() {
        super(Application.class);
        getConfigurationProperties();
    }

    private void getConfigurationProperties() {
        try {
            merchantApiKeyId = Utils.getProperty("merchant-api-key-id", this.getContext());
            merchantApiKeyPassword = Utils.getProperty("merchant-api-key-password", this.getContext());
            merchantAccountNumber = Utils.getProperty("merchant-account-number", this.getContext());
        } catch(IOException ioExp) {
            Utils.debugLog("APPLICATION TEST: IOException: "+ ioExp.getMessage());
        }
    } // end of getConfigurationProperties()

    /**
     * Test Case to Create Single Use Token With All Fields.
     * @throws Exception
     */
    public void testCreateSingleUseToken() throws Exception {

        client = new PaysafeApiClient(merchantApiKeyId, merchantApiKeyPassword, Environment.TEST, merchantAccountNumber);

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

            Assert.assertNotNull("Single Use Token should have an id", sObjResponse.getId());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testCreateSingleUseToken()

    /**
     * Test Case to Create Pay With Google Payment Token
     * @throws Exception
     */
    public void testAndroidPayPaymentToken() throws Exception {
        client = new PaysafeApiClient(merchantApiKeyId, merchantApiKeyPassword, Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService()
                    .createPayWithGooglePaymentToken(
                            SingleUseToken.builder()
                                    .payWithGooglePaymentToken()
                                    .signature("MEYCIQDohrbxD0EQv2Aqt/PSFEMxwy/Vy6LeqPG4OLgfaG3oPAIhAKBEGFoen8QXt3T4Aw/Q5NyZT/krChUL5Jz764ADucYr")
                                    .protocolVersion("ECv1")
                                    .signedMessage("{\\\"encryptedMessage\\\":\\\"aP0ldj2CMXmhWzbty0KDNdavfjArRmzLsFx8D4Jx//kYazevFAfWLl/2F/mqHCqeKoDzjNDNxZplDXJUwKz1WZZ53g/PApxoywAK+0D4z3rQn9vrM8ic9macXXkJJTHT+tXomNp+Cw2GpIIzdtpoh+mGLMSEZBUTr9R4e9am+ur2UrQDxyy+oFusQ2oSK1cD2BHsi++ZUf8q8/6Dvwkk15M8HTA1LA4A1P9Vmpb0lJfatOw2LuGeYBCpXfASaj9odnXJgbQjKlGkhRJzO1HjfboE1TcHoaXJMpOwcjdpeAq+Sav6jdrQ2hI/bLA6cNt+JFJMc0DNKhBjh1DZvV7DXO8yHXLlDzeM7abnNDTPKTh0pCL+xv7Q8mh/zFy5tOuYiH0CRWxWwvXt0stlYKkyyVw7SMH0uc+5Uds2xyTOpACDBE00RE72gyqRZ8KwAUo903IT\\\",\\\"ephemeralPublicKey\\\":\\\"BMfJBn0QJfYSp8tVadhQCEDrg+/dOJgQlU4nJyd905cIlDx/701f8P1Ji+7vN0zMbEX9TDGAMRLwq4jZ1KEaWrs\\\\u003d\\\",\\\"tag\\\":\\\"lI9EwpX1SYByuhgo6BNOSYcRbxELFFVzQDMKHNvvFIw\\\\u003d\\\"}")
                                    .done()
                                    .build());

            Assert.assertNotNull("Payment Token should have an id", sObjResponse.getId());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Optimal Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    }

    /**
     * Test Case to Create Single Use Token With missing Billing Address Zip Code.
     * @throws Exception
     */
    public void testMissingBillingAddressZipCode() throws Exception {

        client = new PaysafeApiClient(merchantApiKeyId, merchantApiKeyPassword, Environment.TEST, merchantAccountNumber);

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
                            .done()
                            .done()
                            .build());

            Assert.assertEquals("5068", sObjResponse.getError().getCode());
            Assert.assertEquals("billingAddress.zip",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            Assert.assertEquals("may not be empty",
                    sObjResponse.getError().getFieldErrors().get(0).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingBillingAddressZipCode()

    /**
     * Test Case to Create Single Use Token with Missing Billing Address Details.
     * @throws Exception
     */
    public void testMissingBillingAddressDetails() throws Exception {

        client = new PaysafeApiClient(merchantApiKeyId, merchantApiKeyPassword, Environment.TEST, merchantAccountNumber);

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
                            .done()
                            .build());

            Assert.assertEquals("5068", sObjResponse.getError().getCode());
            Assert.assertEquals("card.billingAddress",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            Assert.assertEquals("Missing billing address details",
                    sObjResponse.getError().getFieldErrors().get(0).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingBillingAddressDetails()

    /**
     * Test Case to Create Single Use Token with Missing Card Expiry Details.
     * @throws Exception
     */
    public void testMissingCardExpiryDetails() throws Exception {

        client = new PaysafeApiClient(merchantApiKeyId, merchantApiKeyPassword, Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
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

            Assert.assertEquals("5068", sObjResponse.getError().getCode());
            Assert.assertEquals("card.cardExpiry",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            Assert.assertEquals("may not be null",
                    sObjResponse.getError().getFieldErrors().get(0).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingCardExpiryDetails()

    /**
     * Test Case to Create Single Use Token with Missing Card Expiry Month Field.
     * @throws Exception
     */
    public void testMissingCardExpiryMonth() throws Exception {

        client = new PaysafeApiClient(merchantApiKeyId, merchantApiKeyPassword, Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
                            .cardExpiry()
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

            Assert.assertEquals("5068", sObjResponse.getError().getCode());
            Assert.assertEquals("card.cardExpiry.month",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            Assert.assertEquals("must be greater than or equal to 1",
                    sObjResponse.getError().getFieldErrors().get(0).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingCardExpiryMonth()

    /**
     * Test Case to Create Single Use Token with Missing Card Expiry Year Field.
     * @throws Exception
     */
    public void testMissingCardExpiryYear() throws Exception {

        client = new PaysafeApiClient(merchantApiKeyId, merchantApiKeyPassword, Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
                            .cardExpiry()
                            .month(7)
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

            Assert.assertEquals("5068", sObjResponse.getError().getCode());
            Assert.assertEquals("card.cardExpiry.year",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            Assert.assertEquals("must be greater than or equal to 1900",
                    sObjResponse.getError().getFieldErrors().get(0).getError());
            Assert.assertEquals("cardExpiry",
                    sObjResponse.getError().getFieldErrors().get(1).getField());
            Assert.assertEquals("Card expired",
                    sObjResponse.getError().getFieldErrors().get(1).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingCardExpiryYear()

    /**
     * Test Case to Create Single Use Token with Expired Card.
     * @throws Exception
     */
    public void testCreateSingleUseTokenWithExpiredCard() throws Exception {

        client = new PaysafeApiClient(merchantApiKeyId, merchantApiKeyPassword, Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
                            .cardNum("4917480000000008")
                            .cardExpiry()
                            .month(7)
                            .year(2015)
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

            Assert.assertEquals("5068", sObjResponse.getError().getCode());
            Assert.assertEquals("cardExpiry",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            Assert.assertEquals("Card expired",
                    sObjResponse.getError().getFieldErrors().get(0).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testCreateSingleUseTokenWithExpiredCard()

    /**
     * Test Case to Create Single Use Token with Missing Card Number.
     * @throws Exception
     */
    public void testMissingCardNumber() throws Exception {

        client = new PaysafeApiClient(merchantApiKeyId, merchantApiKeyPassword, Environment.TEST, merchantAccountNumber);

        try {
            SingleUseToken sObjResponse = client.customerVaultService().createSingleUseToken(
                    SingleUseToken.builder()
                            .card()
                            .holderName("Mr. John Smith")
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

            Assert.assertEquals("5068", sObjResponse.getError().getCode());
            Assert.assertEquals("cardNum",
                    sObjResponse.getError().getFieldErrors().get(0).getField());
            Assert.assertEquals("Invalid Value",
                    sObjResponse.getError().getFieldErrors().get(0).getError());

        } catch (IOException ioExp) {
            // Log IO Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.DEBUG_LOG_VALUE)
                Utils.debugLog("APPLICATION TEST: " + e.getMessage());
        }
    } // end of testMissingCardNumber()
}