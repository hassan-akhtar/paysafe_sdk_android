package com.paysafe.customervault;

import com.google.gson.annotations.Expose;
import com.paysafe.common.impl.DomainObject;
import com.paysafe.common.impl.GenericBuilder;
import com.paysafe.common.impl.NestedBuilder;

/**
 * Created by asawari.vaidya on 06-09-2017.
 */

public class GooglePayToken implements DomainObject {

    @Expose
    private String signature;
    @Expose
    private String protocolVersion;
    @Expose
    private String signedMessage;
    private String paymentMethod;
    private String authMethod;
    private String tdsCryptogram;
    private String tdsEciIndicator;
    private String messageId;
    private String messageExpiration;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getSignedMessage() {
        return signedMessage;
    }

    public void setSignedMessage(String signedMessage) {
        this.signedMessage = signedMessage;
    }

    /**
     * Get paymentMethod
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Set paymentMethod
     *
     * @param paymentMethod Sets the payment method
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Get authMethod
     */
    public String getAuthMethod() {
        return authMethod;
    }

    /**
     * Set authMethod
     *
     * @param authMethod Sets the authMethod
     */
    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }

    /**
     * Get tdsCryptogram
     */
    public String getTdsCryptogram() {
        return tdsCryptogram;
    }

    /**
     * Set tdsCryptogram
     *
     * @param tdsCryptogram Sets the tdsCryptogram
     */
    public void setTdsCryptogram(String tdsCryptogram) {
        this.tdsCryptogram = tdsCryptogram;
    }

    /**
     * Get tdsEciIndicator
      */
    public String getTdsEciIndicator() {
        return tdsEciIndicator;
    }

    /**
     * Set tdsEciIndicator
     *
     * @param tdsEciIndicator Sets the tdsEciIndicator
     */
    public void setTdsEciIndicator(String tdsEciIndicator) {
        this.tdsEciIndicator = tdsEciIndicator;
    }

    /**
     * Get messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Set messageId
     *
     * @param messageId Sets the messageId
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Get messageExpiration
     */
    public String getMessageExpiration() {
        return messageExpiration;
    }

    /**
     * Set messageExpiration
     *
     * @param messageExpiration Sets the message expiration
     */
    public void setMessageExpiration(String messageExpiration) {
        this.messageExpiration = messageExpiration;
    }

    /**
     * The builder class for GooglePayToken.
     * */
    public static class GooglePayTokenBuilder<BLDRT extends GenericBuilder> extends
            NestedBuilder<GooglePayToken, BLDRT> {

        private final GooglePayToken googlePayToken = new GooglePayToken();

        /**
         * Constructor
         * @param parent Parent object.
         */
        public GooglePayTokenBuilder(final BLDRT parent) {
            super(parent);
        }

        /**
         * Build this GooglePayToken object.
         *
         * @return GooglePayToken object.
         */
        @Override
        public final GooglePayToken build() {
            return googlePayToken;
        } // end of build

        /**
         * Set the signature property for GooglePayToken.
         *
         * @param signature Signature.
         * @return GooglePayTokenBuilder object.
         */
        public final GooglePayTokenBuilder<BLDRT> signature(final String signature) {
            googlePayToken.setSignature(signature);
            return this;
        }

        /**
         * Set the protocolVersion property for GooglePayToken.
         *
         * @param protocolVersion ProtocolVersion.
         * @return GooglePayTokenBuilder object.
         */
        public final GooglePayTokenBuilder<BLDRT> protocolVersion(
                                                                final String protocolVersion) {
            googlePayToken.setProtocolVersion(protocolVersion);
            return this;
        }

        /**
         * Set the signedMessage property for GooglePayToken.
         *
         * @param signedMessage SignedMessage.
         * @return GooglePayTokenBuilder object.
         */
        public final GooglePayTokenBuilder<BLDRT> signedMessage(
                final String signedMessage) {
            googlePayToken.setSignedMessage(signedMessage);
            return this;
        }
    }

} // end of class GooglePayToken
