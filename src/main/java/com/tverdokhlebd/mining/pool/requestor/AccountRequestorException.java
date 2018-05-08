package com.tverdokhlebd.mining.pool.requestor;

import com.tverdokhlebd.mining.commons.http.ErrorCode;
import com.tverdokhlebd.mining.commons.http.RequestException;

/**
 * Exception for working with pool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class AccountRequestorException extends RequestException {

    /** Constant serialVersionUID. */
    private static final long serialVersionUID = 6782611395657196167L;

    /**
     * Creates instance.
     *
     * @param e request exception
     */
    public AccountRequestorException(RequestException e) {
        super(e.getErrorCode(), e.getMessage());
    }

    /**
     * Creates instance.
     *
     * @param errorCode error code
     * @param message detail message
     */
    public AccountRequestorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates instance.
     *
     * @param errorCode error code
     * @param cause cause
     */
    public AccountRequestorException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

}
