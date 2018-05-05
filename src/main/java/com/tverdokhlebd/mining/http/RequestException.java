package com.tverdokhlebd.mining.http;

/**
 * Exception for working with HTTP requests.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class RequestException extends Exception {

    /** Error code. */
    private final ErrorCode errorCode;
    /** Constant serialVersionUID. */
    private static final long serialVersionUID = -4399272030000049516L;

    /**
     * Creates instance.
     *
     * @param errorCode error code
     * @param message detail message
     */
    public RequestException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates instance.
     *
     * @param errorCode error code
     * @param cause cause
     */
    public RequestException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Gets error code.
     *
     * @return error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
