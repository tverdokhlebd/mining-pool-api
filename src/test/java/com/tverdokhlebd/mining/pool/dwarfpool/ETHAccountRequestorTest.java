package com.tverdokhlebd.mining.pool.dwarfpool;

import static com.tverdokhlebd.mining.coin.CoinType.BTC;
import static com.tverdokhlebd.mining.coin.CoinType.ETH;
import static com.tverdokhlebd.mining.pool.PoolType.DWARFPOOL;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;

import com.tverdokhlebd.mining.pool.AccountRequestorException;
import com.tverdokhlebd.mining.pool.Utils;
import com.tverdokhlebd.mining.utils.HashrateUtils;

import okhttp3.OkHttpClient;

/**
 * Tests of ETH account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ETHAccountRequestorTest {

    private final static String WALLET_ADDRESS = "0x4e2c24519354a63c37869d04cefb7d113d17fdc3";

    @Test
    public void testAccount() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"autopayout_from\":\"5.000\",\n" +
                "  \"earning_24_hours\":\"0.01188934\",\n" +
                "  \"error\":false,\n" +
                "  \"immature_earning\":0.000637337646,\n" +
                "  \"last_payment_amount\":0,\n" +
                "  \"last_payment_date\":null,\n" +
                "  \"last_share_date\":\"Sat, 05 May 2018 22:00:55 GMT\",\n" +
                "  \"payout_daily\":false,\n" +
                "  \"payout_request\":false,\n" +
                "  \"total_hashrate\":173.67,\n" +
                "  \"total_hashrate_calculated\":173.24,\n" +
                "  \"transferring_to_balance\":0,\n" +
                "  \"wallet\":\"0x4e2c24519354a63c37869d04cefb7d113d17fdc3\",\n" +
                "  \"wallet_balance\":\"1.44245810\",\n" +
                "  \"workers\":{  \n" +
                "    \"dmtry\":{  \n" +
                "      \"alive\":true,\n" +
                "      \"hashrate\":173.67,\n" +
                "      \"hashrate_below_threshold\":false,\n" +
                "      \"hashrate_calculated\":173.24,\n" +
                "      \"last_submit\":\"Sat, 05 May 2018 22:00:55 GMT\",\n" +
                "      \"second_since_submit\":435,\n" +
                "      \"worker\":\"dmtry\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        BigDecimal expectedBalance = BigDecimal.valueOf(1.44245810);
        BigDecimal expectedHashrate = HashrateUtils.convertMegaHashesToHashes(BigDecimal.valueOf(173.67));
        Utils.testAccount(DWARFPOOL, httpClient, ETH, WALLET_ADDRESS, expectedBalance, expectedHashrate);
    }

    @Test(expected = AccountRequestorException.class)
    public void testApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"error\":true,\n" +
                "  \"error_code\":\"API_DOWN\"\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        Utils.testApiError(DWARFPOOL, httpClient, ETH, WALLET_ADDRESS, "API_DOWN");
    }

    @Test(expected = AccountRequestorException.class)
    public void testInternalServerError() throws AccountRequestorException {
        Utils.testInternalServerError(DWARFPOOL, ETH, WALLET_ADDRESS);
    }

    @Test(expected = AccountRequestorException.class)
    public void testEmptyResponse() throws AccountRequestorException {
        Utils.testEmptyResponse(DWARFPOOL, ETH, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedCoin() throws AccountRequestorException {
        Utils.testUnsupportedCoin(DWARFPOOL, BTC, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(DWARFPOOL, ETH, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(DWARFPOOL, ETH, null);
    }

}
