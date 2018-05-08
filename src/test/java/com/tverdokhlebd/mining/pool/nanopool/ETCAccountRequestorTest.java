package com.tverdokhlebd.mining.pool.nanopool;

import static com.tverdokhlebd.mining.commons.coin.CoinType.BTC;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ETC;
import static com.tverdokhlebd.mining.pool.PoolType.NANOPOOL;
import static com.tverdokhlebd.mining.commons.utils.HttpClientUtils.MEDIA_JSON;
import static okhttp3.Protocol.HTTP_2;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;

import com.tverdokhlebd.mining.pool.Utils;
import com.tverdokhlebd.mining.pool.requestor.AccountRequestorException;
import com.tverdokhlebd.mining.commons.utils.HashrateUtils;
import com.tverdokhlebd.mining.commons.utils.HttpClientUtils;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Tests of ETC account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ETCAccountRequestorTest {

    private final static String WALLET_ADDRESS = "0xd477ed559eac657957a0e77323b240d07f885cbb";

    @Test
    public void testAccount() throws AccountRequestorException {
        JSONObject responseBalance = new JSONObject("{  \n" +
                "  \"status\":true,\n" +
                "  \"data\":0.354999056034479\n" +
                "}");
        JSONObject responseReportedHashrate = new JSONObject("{  \n" +
                "  \"status\":true,\n" +
                "  \"data\":7663.625\n" +
                "}");
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            ResponseBody body = null;
            if (request.url().encodedPath().indexOf("balance") != -1) {
                body = ResponseBody.create(MEDIA_JSON, responseBalance.toString());
            } else {
                body = ResponseBody.create(MEDIA_JSON, responseReportedHashrate.toString());
            }
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(200).message("").build();
        };
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
        BigDecimal expectedBalance = BigDecimal.valueOf(0.354999056034479);
        BigDecimal expectedHashrate = HashrateUtils.convertMegaHashesToHashes(BigDecimal.valueOf(7663.625));
        Utils.testAccount(NANOPOOL, httpClient, ETC, WALLET_ADDRESS, expectedBalance, expectedHashrate);
    }

    @Test(expected = AccountRequestorException.class)
    public void testApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"status\":false,\n" +
                "  \"error\":\"No data found\"\n" +
                "}");
        OkHttpClient httpClient = HttpClientUtils.createHttpClient(response.toString(), 200);
        Utils.testApiError(NANOPOOL, httpClient, ETC, WALLET_ADDRESS, "No data found");
    }

    @Test(expected = AccountRequestorException.class)
    public void testInternalServerError() throws AccountRequestorException {
        Utils.testInternalServerError(NANOPOOL, ETC, WALLET_ADDRESS);
    }

    @Test(expected = AccountRequestorException.class)
    public void testEmptyResponse() throws AccountRequestorException {
        Utils.testEmptyResponse(NANOPOOL, ETC, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedCoin() throws AccountRequestorException {
        Utils.testUnsupportedCoin(NANOPOOL, BTC, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(NANOPOOL, ETC, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(NANOPOOL, ETC, null);
    }

}
