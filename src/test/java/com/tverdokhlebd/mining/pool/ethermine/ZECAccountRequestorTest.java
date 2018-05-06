package com.tverdokhlebd.mining.pool.ethermine;

import static com.tverdokhlebd.mining.coin.CoinType.BTC;
import static com.tverdokhlebd.mining.coin.CoinType.ZEC;
import static com.tverdokhlebd.mining.pool.PoolType.ETHERMINE;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;

import com.tverdokhlebd.mining.pool.Utils;
import com.tverdokhlebd.mining.pool.requestor.AccountRequestorException;

import okhttp3.OkHttpClient;

/**
 * Tests of ZEC account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ZECAccountRequestorTest {

    private final static String WALLET_ADDRESS = "t1SNzej5zM69o8Y6xg1o9sWP95hVn42uEq6";

    @Test
    public void testAccount() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"status\":\"OK\",\n" +
                "  \"data\":{  \n" +
                "    \"time\":1525603200,\n" +
                "    \"lastSeen\":1525603271,\n" +
                "    \"reportedHashrate\":11386.666666666666,\n" +
                "    \"currentHashrate\":11384.666666666666,\n" +
                "    \"validShares\":427,\n" +
                "    \"invalidShares\":2,\n" +
                "    \"staleShares\":0,\n" +
                "    \"averageHashrate\":10628.888888888887,\n" +
                "    \"activeWorkers\":6,\n" +
                "    \"unpaid\":853313,\n" +
                "    \"unconfirmed\":2181844,\n" +
                "    \"coinsPerMin\":0.00010473639254208149,\n" +
                "    \"usdPerMin\":0.030959030271513865,\n" +
                "    \"btcPerMin\":0.0000032101704314147977\n" +
                "  }\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        BigDecimal expectedBalance = BigDecimal.valueOf(0.000000000000853313);
        BigDecimal expectedHashrate = BigDecimal.valueOf(11386.666666666666);
        Utils.testAccount(ETHERMINE, httpClient, ZEC, WALLET_ADDRESS, expectedBalance, expectedHashrate);
    }

    @Test(expected = AccountRequestorException.class)
    public void testApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"status\":\"ERROR\",\n" +
                "  \"error\":\"Invalid address\"\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        Utils.testApiError(ETHERMINE, httpClient, ZEC, WALLET_ADDRESS, "Invalid address");
    }

    @Test(expected = AccountRequestorException.class)
    public void testNoDataError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"status\":\"OK\",\n" +
                "  \"data\":\"NO DATA\"\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        Utils.testApiError(ETHERMINE, httpClient, ZEC, WALLET_ADDRESS, "NO DATA");
    }

    @Test(expected = AccountRequestorException.class)
    public void testInternalServerError() throws AccountRequestorException {
        Utils.testInternalServerError(ETHERMINE, ZEC, WALLET_ADDRESS);
    }

    @Test(expected = AccountRequestorException.class)
    public void testEmptyResponse() throws AccountRequestorException {
        Utils.testEmptyResponse(ETHERMINE, ZEC, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedCoin() throws AccountRequestorException {
        Utils.testUnsupportedCoin(ETHERMINE, BTC, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(ETHERMINE, ZEC, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(ETHERMINE, ZEC, null);
    }

}
