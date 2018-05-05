package com.tverdokhlebd.mining.pool;

import com.tverdokhlebd.mining.http.ErrorCode;
import com.tverdokhlebd.mining.http.RequestException;

/**
 * Exception for working with pool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
class AccountRequestorException extends RequestException {

    /** Constant serialVersionUID. */
    private static final long serialVersionUID = 6782611395657196167L;

    /**
     * Creates instance.
     *
     * @param errorCode error code
     * @param message detail message
     */
    AccountRequestorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates instance.
     *
     * @param errorCode error code
     * @param cause cause
     */
    AccountRequestorException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

}
