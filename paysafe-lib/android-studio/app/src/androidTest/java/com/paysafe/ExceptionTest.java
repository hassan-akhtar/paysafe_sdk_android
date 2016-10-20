package com.paysafe;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.paysafe.utils.Constants;
import com.paysafe.common.PaysafeException;
import com.paysafe.customervault.SingleUseToken;

import junit.framework.Assert;

import java.io.IOException;

/**
 * Created by Asawari.Vaidya on 23-09-2015.
 */
public class ExceptionTest extends ApplicationTestCase<Application> {

    // Paysafe Api Client Object
    private static PaysafeApiClient client;

    // Credentials
    private final String apiKey = "OT-16156";
    private final String apiPassword = "B-qa2-0-552b9bcf-0-302c02144ed8f9b8aea9d65b44b77a16a81ec5f9ab916f8c02142b4e69b3a272b866b1e263b0b0c7a925a8945418";

    // Merchant Account Number
    private final String accountNumber = "1001187820";

    // Log Tag
    private static final String LOG_EXCEPTION_TEST = "EXCEPTION TEST";

    public ExceptionTest() {
        super(Application.class);
    }

    /**
     * Test Case to Create Single Use Token with Missing or Invalid Merchant Account Number.
     * @throws Exception
     */
    public void testMissingOrInvalidAccount() throws Exception {

        client = new PaysafeApiClient(apiKey, apiPassword, Environment.TEST);

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
            if(Constants.TAG_LOG)
                Log.e(LOG_EXCEPTION_TEST, ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            Assert.assertNotNull("Missing or Invalid Account.", oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_EXCEPTION_TEST, e.getMessage());
        }
    } // end of testMissingOrInvalidAccount

    /**
     * Test Case to Create Single Use Token with Invalid Authentication Credentials.
     * @throws Exception
     */
    public void testInvalidAuthenticationCredentials() throws Exception {
        client = new PaysafeApiClient("username", "password", Environment.TEST, accountNumber);

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
            if(Constants.TAG_LOG)
                Log.e(LOG_EXCEPTION_TEST, ioExp.getMessage());
        } catch (PaysafeException oExp) {
            // Log Paysafe Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_EXCEPTION_TEST, oExp.getMessage());
        } catch (Exception e) {
            // Log Exception
            if(Constants.TAG_LOG)
                Log.e(LOG_EXCEPTION_TEST, e.getMessage());
        }
    } // end of testInvalidAuthenticationCredentials()
}
