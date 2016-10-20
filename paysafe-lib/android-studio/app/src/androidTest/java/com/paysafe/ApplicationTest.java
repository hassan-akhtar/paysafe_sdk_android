package com.paysafe;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.paysafe.utils.Constants;
import com.paysafe.common.PaysafeException;
import com.paysafe.customervault.SingleUseToken;
import com.paysafe.PaysafeApiClient;

import junit.framework.Assert;

import java.io.IOException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    // Paysafe Api Client Object
    private static PaysafeApiClient client;

    // Credentials
    private final String apiKey = "OT-16156";
    private final String apiPassword = "B-qa2-0-552b9bcf-0-302c02144ed8f9b8aea9d65b44b77a16a81ec5f9ab916f8c02142b4e69b3a272b866b1e263b0b0c7a925a8945418";

    // Merchant Account Number
    private final String accountNumber = "1001187820";

    // Log Tag
    private static final String LOG_APPLICATION_TEST = "APPLICATION TEST";

    public ApplicationTest() {
        super(Application.class);
    }

    /**
     * Test Case to Create Single Use Token With All Fields.
     * @throws Exception
     */
    public void testCreateSingleUseToken() throws Exception {

        client = new PaysafeApiClient(apiKey, apiPassword, Environment.TEST, accountNumber);

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
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, e.getMessage());
        }
    } // end of testCreateSingleUseToken()

    /**
     * Test Case to Create Single Use Token With missing Billing Address Zip Code.
     * @throws Exception
     */
    public void testMissingBillingAddressZipCode() throws Exception {

        client = new PaysafeApiClient(apiKey, apiPassword, Environment.TEST, accountNumber);

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
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, e.getMessage());
        }
    } // end of testMissingBillingAddressZipCode()

    /**
     * Test Case to Create Single Use Token with Missing Billing Address Details.
     * @throws Exception
     */
    public void testMissingBillingAddressDetails() throws Exception {

        client = new PaysafeApiClient(apiKey, apiPassword, Environment.TEST, accountNumber);

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
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, e.getMessage());
        }
    } // end of testMissingBillingAddressDetails()

    /**
     * Test Case to Create Single Use Token with Missing Card Expiry Details.
     * @throws Exception
     */
    public void testMissingCardExpiryDetails() throws Exception {

        client = new PaysafeApiClient(apiKey, apiPassword, Environment.TEST, accountNumber);

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
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, e.getMessage());
        }
    } // end of testMissingCardExpiryDetails()

    /**
     * Test Case to Create Single Use Token with Missing Card Expiry Month Field.
     * @throws Exception
     */
    public void testMissingCardExpiryMonth() throws Exception {

        client = new PaysafeApiClient(apiKey, apiPassword, Environment.TEST, accountNumber);

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
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, e.getMessage());
        }
    } // end of testMissingCardExpiryMonth()

    /**
     * Test Case to Create Single Use Token with Missing Card Expiry Year Field.
     * @throws Exception
     */
    public void testMissingCardExpiryYear() throws Exception {

        client = new PaysafeApiClient(apiKey, apiPassword, Environment.TEST, accountNumber);

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
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, e.getMessage());
        }
    } // end of testMissingCardExpiryYear()

    /**
     * Test Case to Create Single Use Token with Expired Card.
     * @throws Exception
     */
    public void testCreateSingleUseTokenWithExpiredCard() throws Exception {

        client = new PaysafeApiClient(apiKey, apiPassword, Environment.TEST, accountNumber);

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
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, e.getMessage());
        }
    } // end of testCreateSingleUseTokenWithExpiredCard()

    /**
     * Test Case to Create Single Use Token with Missing Card Number.
     * @throws Exception
     */
    public void testMissingCardNumber() throws Exception {

        client = new PaysafeApiClient(apiKey, apiPassword, Environment.TEST, accountNumber);

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
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_APPLICATION_TEST, e.getMessage());
        }
    } // end of testMissingCardNumber()
}