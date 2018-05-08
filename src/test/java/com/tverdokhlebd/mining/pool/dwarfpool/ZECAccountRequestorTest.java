package com.tverdokhlebd.mining.pool.dwarfpool;

import static com.tverdokhlebd.mining.commons.coin.CoinType.BTC;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ZEC;
import static com.tverdokhlebd.mining.pool.PoolType.DWARFPOOL;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;

import com.tverdokhlebd.mining.pool.Utils;
import com.tverdokhlebd.mining.pool.requestor.AccountRequestorException;
import com.tverdokhlebd.mining.commons.utils.HttpClientUtils;

import okhttp3.OkHttpClient;

/**
 * Tests of ZEC account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ZECAccountRequestorTest {

    private final static String WALLET_ADDRESS = "t1VUThVMRgWCWKHQJ2nSWiMiVt2pKCXJLS4";

    @Test
    public void testAccount() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"autopayout_from\":\"0.010\",\n" +
                "  \"earning_24_hours\":\"?\",\n" +
                "  \"error\":false,\n" +
                "  \"immature_earning\":0,\n" +
                "  \"last_payment_amount\":\"1.08746122\",\n" +
                "  \"last_payment_date\":\"Sun, 06 May 2018 01:13:21 GMT\",\n" +
                "  \"last_share_date\":\"Sun, 06 May 2018 09:29:00 GMT\",\n" +
                "  \"payout_daily\":false,\n" +
                "  \"payout_request\":false,\n" +
                "  \"total_hashrate\":229561.01,\n" +
                "  \"total_hashrate_calculated\":229561.01,\n" +
                "  \"transferring_to_balance\":0,\n" +
                "  \"wallet\":\"0xt1VUThVMRgWCWKHQJ2nSWiMiVt2pKCXJLS4\",\n" +
                "  \"wallet_balance\":\"0.00000001\",\n" +
                "  \"workers\":{  \n" +
                "    \"\":{  \n" +
                "      \"alive\":true,\n" +
                "      \"hashrate\":229561.01,\n" +
                "      \"hashrate_below_threshold\":false,\n" +
                "      \"hashrate_calculated\":229561.01,\n" +
                "      \"last_submit\":\"Sun, 06 May 2018 09:29:00 GMT\",\n" +
                "      \"second_since_submit\":82,\n" +
                "      \"worker\":\"\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
        OkHttpClient httpClient = HttpClientUtils.createHttpClient(response.toString(), 200);
        BigDecimal expectedBalance = BigDecimal.valueOf(0.00000001);
        BigDecimal expectedHashrate = BigDecimal.valueOf(229561.01);
        Utils.testAccount(DWARFPOOL, httpClient, ZEC, WALLET_ADDRESS, expectedBalance, expectedHashrate);
    }

    @Test(expected = AccountRequestorException.class)
    public void testApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"error\":true,\n" +
                "  \"error_code\":\"API_DOWN\"\n" +
                "}");
        OkHttpClient httpClient = HttpClientUtils.createHttpClient(response.toString(), 200);
        Utils.testApiError(DWARFPOOL, httpClient, ZEC, WALLET_ADDRESS, "API_DOWN");
    }

    @Test(expected = AccountRequestorException.class)
    public void testInternalServerError() throws AccountRequestorException {
        Utils.testInternalServerError(DWARFPOOL, ZEC, WALLET_ADDRESS);
    }

    @Test(expected = AccountRequestorException.class)
    public void testEmptyResponse() throws AccountRequestorException {
        Utils.testEmptyResponse(DWARFPOOL, ZEC, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedCoin() throws AccountRequestorException {
        Utils.testUnsupportedCoin(DWARFPOOL, BTC, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(DWARFPOOL, ZEC, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(DWARFPOOL, ZEC, null);
    }

}
