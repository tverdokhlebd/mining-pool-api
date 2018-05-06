package com.tverdokhlebd.mining.pool;

import static com.tverdokhlebd.mining.http.ErrorCode.API_ERROR;
import static com.tverdokhlebd.mining.http.ErrorCode.HTTP_ERROR;
import static com.tverdokhlebd.mining.http.ErrorCode.PARSE_ERROR;
import static okhttp3.Protocol.HTTP_2;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.tverdokhlebd.mining.coin.CoinType;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Utils for tests.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class Utils {

    /** JSON media type. */
    public static final MediaType MEDIA_JSON = MediaType.parse("application/json");

    /**
     * Tests account.
     *
     * @param poolType type of pool
     * @param httpClient HTTP client
     * @param coinType type of coin
     * @param walletAddress wallet address
     * @param expectedBalance expected wallet balance
     * @param expectedHashrate expected reported hashrate
     * @throws AccountRequestorException if there is any error in account requesting
     */
    public static void testAccount(PoolType poolType, OkHttpClient httpClient, CoinType coinType, String walletAddress,
            BigDecimal expectedBalance, BigDecimal expectedHashrate)
            throws AccountRequestorException {
        AccountRequestor accountRequestor = AccountRequestorFactory.create(poolType, httpClient, false);
        Account account = accountRequestor.requestAccount(coinType, walletAddress);
        assertEquals(walletAddress, account.getWalletAddress());
        assertEquals(expectedBalance, account.getWalletBalance());
        assertEquals(expectedHashrate, account.getReportedHashrate());
    }

    /**
     * Tests API error.
     *
     * @param poolType type of pool
     * @param httpClient HTTP client
     * @param coinType type of coin
     * @param walletAddress wallet address
     * @param expectedErrorMessage expected error message
     * @throws AccountRequestorException if there is any error in account requesting
     */
    public static void testApiError(PoolType poolType, OkHttpClient httpClient, CoinType coinType, String walletAddress,
            String expectedErrorMessage) throws AccountRequestorException {
        AccountRequestor accountRequestor = AccountRequestorFactory.create(poolType, httpClient, false);
        try {
            accountRequestor.requestAccount(coinType, walletAddress);
        } catch (AccountRequestorException e) {
            assertEquals(API_ERROR, e.getErrorCode());
            assertEquals(expectedErrorMessage, e.getMessage());
            throw e;
        }
    }

    /**
     * Tests internal server error.
     *
     * @param poolType type of pool
     * @param coinType type of coin
     * @param walletAddress wallet address
     * @throws AccountRequestorException if there is any error in account requesting
     */
    public static void testInternalServerError(PoolType poolType, CoinType coinType, String walletAddress)
            throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 500);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(poolType, httpClient, false);
        try {
            accountRequestor.requestAccount(coinType, walletAddress);
        } catch (AccountRequestorException e) {
            assertEquals(HTTP_ERROR, e.getErrorCode());
            throw e;
        }
    }

    /**
     * Tests empty response.
     *
     * @param poolType type of pool
     * @param coinType type of coin
     * @param walletAddress wallet address
     * @throws AccountRequestorException if there is any error in account requesting
     */
    public static void testEmptyResponse(PoolType poolType, CoinType coinType, String walletAddress)
            throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(poolType, httpClient, false);
        try {
            accountRequestor.requestAccount(coinType, walletAddress);
        } catch (AccountRequestorException e) {
            assertEquals(PARSE_ERROR, e.getErrorCode());
            throw e;
        }
    }

    /**
     * Tests unsupported coin.
     *
     * @param poolType type of pool
     * @param coinType type of coin
     * @param walletAddress wallet address
     * @throws AccountRequestorException if there is any error in account requesting
     */
    public static void testUnsupportedCoin(PoolType poolType, CoinType coinType, String walletAddress)
            throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(poolType, httpClient, false);
        try {
            accountRequestor.requestAccount(coinType, walletAddress);
        } catch (IllegalArgumentException e) {
            assertEquals(coinType.name() + " is not supported", e.getMessage());
            throw e;
        }
    }

    /**
     * Tests empty wallet address.
     *
     * @param poolType type of pool
     * @param coinType type of coin
     * @param walletAddress wallet address
     * @throws AccountRequestorException if there is any error in account requesting
     */
    public static void testEmptyWalletAddress(PoolType poolType, CoinType coinType, String walletAddress)
            throws AccountRequestorException {
        OkHttpClient httpClient = Utils.getHttpClient(new JSONObject().toString(), 200);
        AccountRequestor accountRequestor = AccountRequestorFactory.create(poolType, httpClient, false);
        try {
            accountRequestor.requestAccount(coinType, walletAddress);
        } catch (IllegalArgumentException e) {
            assertEquals("Wallet address is null or empty", e.getMessage());
            throw e;
        }
    }

    /**
     * Gets HTTP client with custom JSON response.
     *
     * @param response JSON response
     * @param responseCode HTTP code
     * @return HTTP client with custom JSON response
     */
    public static OkHttpClient getHttpClient(String response, int responseCode) {
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            ResponseBody body = ResponseBody.create(MEDIA_JSON, response);
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(responseCode).message("").build();
        };
        return new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
    }

}