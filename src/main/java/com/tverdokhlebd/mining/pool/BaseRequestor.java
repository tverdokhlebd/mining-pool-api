package com.tverdokhlebd.mining.pool;

import static com.tverdokhlebd.mining.http.ErrorCode.HTTP_ERROR;

import java.io.IOException;

import com.tverdokhlebd.mining.http.RequestException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Base requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 * @param <T> result data
 */
abstract class BaseRequestor<T> {

    /** HTTP client. */
    private final OkHttpClient httpClient;

    /**
     * Creates instance.
     *
     * @param httpClient HTTP client
     */
    BaseRequestor(OkHttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    /**
     * Makes request to specific url.
     *
     * @param url url address
     * @param requestName name of request
     * @param result result data
     * @throws RequestException if there is any error in making request
     */
    void request(String url, String requestName, T result) throws RequestException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RequestException(HTTP_ERROR, response.message());
            }
            try (ResponseBody body = response.body()) {
                checkApiError(body, requestName);
                parseResponse(body, requestName, result);
            }
        } catch (IOException e) {
            throw new RequestException(HTTP_ERROR, e);
        }
    }

    /**
     * Checks presence of API error.
     *
     * @param responseBody response body
     * @param requestName name of request
     * @throws RequestException if there is any API error in response
     */
    protected abstract void checkApiError(ResponseBody responseBody, String requestName) throws RequestException;

    /**
     * Parses response and fills result.
     *
     * @param responseBody response body
     * @param requestName name of request
     * @param result result data
     * @throws RequestException if there is any error in parsing response
     */
    protected abstract void parseResponse(ResponseBody responseBody, String requestName, T result) throws RequestException;

}
