package com.tverdokhlebd.mining.pool.dwarfpool;

import static com.tverdokhlebd.mining.coin.CoinType.BTC;
import static com.tverdokhlebd.mining.coin.CoinType.XMR;
import static com.tverdokhlebd.mining.pool.PoolType.DWARFPOOL;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;

import com.tverdokhlebd.mining.pool.Utils;
import com.tverdokhlebd.mining.pool.requestor.AccountRequestorException;
import com.tverdokhlebd.mining.utils.HashrateUtils;

import okhttp3.OkHttpClient;

/**
 * Tests of XMR account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class XMRAccountRequestorTest {

    private final static String WALLET_ADDRESS =
            "4Hm3YrYNgczRAP7jbGCZ7vA8XwbBR8DWMU7Bm9FKZqjxQXPPcwMP1kDbK3mtBSdt2c6TmLCPiMSXa39uBiEBwkg4FYkH99YeMAf2kMiE1B";

    @Test
    public void testAccount() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"autopayout_from\":\"2.000\",\n" +
                "  \"earning_24_hours\":\"0.00235710\",\n" +
                "  \"error\":false,\n" +
                "  \"immature_earning\":0.0008070795484999999,\n" +
                "  \"last_payment_amount\":0,\n" +
                "  \"last_payment_date\":null,\n" +
                "  \"last_share_date\":\"Sun, 06 May 2018 09:03:00 GMT\",\n" +
                "  \"payout_daily\":false,\n" +
                "  \"payout_request\":false,\n" +
                "  \"total_hashrate\":0.73,\n" +
                "  \"total_hashrate_calculated\":0.55,\n" +
                "  \"transferring_to_balance\":0,\n" +
                "  \"wallet\":\"0x4Hm3YrYNgczRAP7jbGCZ7vA8XwbBR8DWMU7Bm9FKZqjxQXPPcwMP1kDbK3mtBSdt2c6TmLCPiMSXa39uBiEBwkg4FYkH99YeMAf2kMiE1B\",\n"
                +
                "  \"wallet_balance\":\"0.03140318\",\n" +
                "  \"workers\":{  \n" +
                "\n" +
                "  }\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        BigDecimal expectedBalance = BigDecimal.valueOf(0.03140318);
        BigDecimal expectedHashrate = HashrateUtils.convertKiloHashesToHashes(BigDecimal.valueOf(0.73));
        Utils.testAccount(DWARFPOOL, httpClient, XMR, WALLET_ADDRESS, expectedBalance, expectedHashrate);
    }

    @Test(expected = AccountRequestorException.class)
    public void testApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"error\":true,\n" +
                "  \"error_code\":\"API_DOWN\"\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        Utils.testApiError(DWARFPOOL, httpClient, XMR, WALLET_ADDRESS, "API_DOWN");
    }

    @Test(expected = AccountRequestorException.class)
    public void testInternalServerError() throws AccountRequestorException {
        Utils.testInternalServerError(DWARFPOOL, XMR, WALLET_ADDRESS);
    }

    @Test(expected = AccountRequestorException.class)
    public void testEmptyResponse() throws AccountRequestorException {
        Utils.testEmptyResponse(DWARFPOOL, XMR, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedCoin() throws AccountRequestorException {
        Utils.testUnsupportedCoin(DWARFPOOL, BTC, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(DWARFPOOL, XMR, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(DWARFPOOL, XMR, null);
    }

}
