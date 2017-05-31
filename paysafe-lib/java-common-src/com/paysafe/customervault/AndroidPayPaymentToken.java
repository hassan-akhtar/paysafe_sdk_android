/*
 * Copyright (c) 2015 Optimal Payments
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.paysafe.customervault;

import com.google.gson.annotations.Expose;
import com.paysafe.common.impl.GenericBuilder;
import com.paysafe.common.impl.NestedBuilder;
import com.paysafe.common.impl.DomainObject;

/**
 * Created by asawari.vaidya on 07-04-2017.
 */

public class AndroidPayPaymentToken implements DomainObject {

    @Expose
    private String encryptedMessage;
    @Expose
    private String ephemeralPublicKey;
    @Expose
    private String tag;

    /**
     * Get Encrypted Message
     *
     * @return Encrypted Message
     * */
    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    /**
     * Set Encrypted Message
     *
     * @param encryptedMessage Sets the Encrypted Message.
     * */
    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    /**
     * Get Ephemeral Public Key
     *
     * @return Ephemeral Public Key
     * */
    public String getEphemeralPublicKey() {
        return ephemeralPublicKey;
    }

    /**
     * Set Ephemeral Public Key
     *
     * @param ephemeralPublicKey Sets the Ephemeral Public Key.
     * */
    public void setEphemeralPublicKey(String ephemeralPublicKey) {
        this.ephemeralPublicKey = ephemeralPublicKey;
    }

    /**
     * Get tag
     *
     * @return tag
     * */
    public String getTag() {
        return tag;
    }

    /**
     * Set tag
     *
     * @param tag Sets the tag.
     * */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * The builder class for AndroidPayPaymentToken.
     * */
    public static class AndroidPayPaymentTokenBuilder<BLDRT extends GenericBuilder> extends
            NestedBuilder<AndroidPayPaymentToken, BLDRT> {

        private final AndroidPayPaymentToken androidPayPaymentToken = new AndroidPayPaymentToken();

        /**
         * Constructor
         * @param parent Parent object.
         */
        public AndroidPayPaymentTokenBuilder(final BLDRT parent) {
            super(parent);
        }

        /**
         * Build this AndroidPayPaymentToken object.
         *
         * @return AndroidPayPaymentToken object.
         */
        @Override
        public final AndroidPayPaymentToken build() {
            return androidPayPaymentToken;
        } // end of build

        /**
         * Set the encryptedMessage property for AndroidPayPaymentToken.
         *
         * @param encryptedMessage Encrypted Message.
         * @return AndroidPayPaymentTokenBuilder object.
         */
        public final AndroidPayPaymentTokenBuilder<BLDRT> encryptedMessage(final String encryptedMessage) {
            androidPayPaymentToken.setEncryptedMessage(encryptedMessage);
            return this;
        }

        /**
         * Set the ephemeralPublicKey property for AndroidPayPaymentToken.
         *
         * @param ephemeralPublicKey Ephemeral Public Key.
         * @return AndroidPayPaymentTokenBuilder object.
         */
        public final AndroidPayPaymentTokenBuilder<BLDRT> ephemeralPublicKey(final String ephemeralPublicKey) {
            androidPayPaymentToken.setEphemeralPublicKey(ephemeralPublicKey);
            return this;
        }

        /**
         * Set the tag property for AndroidPayPaymentToken.
         *
         * @param tag Tag.
         * @return AndroidPayPaymentTokenBuilder object.
         */
        public final AndroidPayPaymentTokenBuilder<BLDRT> tag(final String tag) {
            androidPayPaymentToken.setTag(tag);
            return this;
        }
    }
} // end of class AndroidPayPaymentToken
