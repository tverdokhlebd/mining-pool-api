package com.tverdokhlebd.mining.pool.ethermine;

import static com.tverdokhlebd.mining.coin.CoinType.BTC;
import static com.tverdokhlebd.mining.coin.CoinType.ETH;
import static com.tverdokhlebd.mining.pool.PoolType.ETHERMINE;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;

import com.tverdokhlebd.mining.pool.AccountRequestorException;
import com.tverdokhlebd.mining.pool.Utils;

import okhttp3.OkHttpClient;

/**
 * Tests of ETH account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ETHAccountRequestorTest {

    private final static String WALLET_ADDRESS = "0xb7b03ce91e5826bd8d223b15dedbdb8ef4ddd4a8";

    @Test
    public void testAccount() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"status\":\"OK\",\n" +
                "  \"data\":{  \n" +
                "    \"time\":1525602600,\n" +
                "    \"lastSeen\":1525602600,\n" +
                "    \"reportedHashrate\":3142061333333.333,\n" +
                "    \"currentHashrate\":3142061333333.333,\n" +
                "    \"validShares\":2745560,\n" +
                "    \"invalidShares\":0,\n" +
                "    \"staleShares\":126608,\n" +
                "    \"averageHashrate\":2105501953317.9023,\n" +
                "    \"activeWorkers\":2,\n" +
                "    \"unpaid\":1177815222280930600,\n" +
                "    \"unconfirmed\":null,\n" +
                "    \"coinsPerMin\":0.10422546384305864,\n" +
                "    \"usdPerMin\":81.92329908992095,\n" +
                "    \"btcPerMin\":0.008527727451639058\n" +
                "  }\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        BigDecimal expectedBalance = BigDecimal.valueOf(1.1778152222809306);
        BigDecimal expectedHashrate = BigDecimal.valueOf(3142061333333.333);
        Utils.testAccount(ETHERMINE, httpClient, ETH, WALLET_ADDRESS, expectedBalance, expectedHashrate);
    }

    @Test(expected = AccountRequestorException.class)
    public void testApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"status\":\"ERROR\",\n" +
                "  \"error\":\"Invalid address\"\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        Utils.testApiError(ETHERMINE, httpClient, ETH, WALLET_ADDRESS, "Invalid address");
    }

    @Test(expected = AccountRequestorException.class)
    public void testNoDataError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"status\":\"OK\",\n" +
                "  \"data\":\"NO DATA\"\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        Utils.testApiError(ETHERMINE, httpClient, ETH, WALLET_ADDRESS, "NO DATA");
    }

    @Test(expected = AccountRequestorException.class)
    public void testInternalServerError() throws AccountRequestorException {
        Utils.testInternalServerError(ETHERMINE, ETH, WALLET_ADDRESS);
    }

    @Test(expected = AccountRequestorException.class)
    public void testEmptyResponse() throws AccountRequestorException {
        Utils.testEmptyResponse(ETHERMINE, ETH, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedCoin() throws AccountRequestorException {
        Utils.testUnsupportedCoin(ETHERMINE, BTC, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(ETHERMINE, ETH, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(ETHERMINE, ETH, null);
    }

}
