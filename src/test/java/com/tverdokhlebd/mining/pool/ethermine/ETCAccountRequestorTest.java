package com.tverdokhlebd.mining.pool.ethermine;

import static com.tverdokhlebd.mining.coin.CoinType.BTC;
import static com.tverdokhlebd.mining.coin.CoinType.ETC;
import static com.tverdokhlebd.mining.pool.PoolType.ETHERMINE;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;

import com.tverdokhlebd.mining.pool.AccountRequestorException;
import com.tverdokhlebd.mining.pool.Utils;

import okhttp3.OkHttpClient;

/**
 * Tests of ETC account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class ETCAccountRequestorTest {

    private final static String WALLET_ADDRESS = "0x6DA33282555cD3476a0e01A94eeadf01E4941544";

    @Test
    public void testAccount() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"status\":\"OK\",\n" +
                "  \"data\":{  \n" +
                "    \"time\":1525602600,\n" +
                "    \"lastSeen\":1525602594,\n" +
                "    \"reportedHashrate\":1228822767,\n" +
                "    \"currentHashrate\":1213277777.777778,\n" +
                "    \"validShares\":1077,\n" +
                "    \"invalidShares\":0,\n" +
                "    \"staleShares\":23,\n" +
                "    \"averageHashrate\":1188068287.037037,\n" +
                "    \"activeWorkers\":10,\n" +
                "    \"unpaid\":513627109343957900,\n" +
                "    \"unconfirmed\":null,\n" +
                "    \"coinsPerMin\":0.0025146263736722857,\n" +
                "    \"usdPerMin\":0.061935247583548396,\n" +
                "    \"btcPerMin\":0.00000642487038473269\n" +
                "  }\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        BigDecimal expectedBalance = BigDecimal.valueOf(0.5136271093439579);
        BigDecimal expectedHashrate = BigDecimal.valueOf(1228822767);
        Utils.testAccount(ETHERMINE, httpClient, ETC, WALLET_ADDRESS, expectedBalance, expectedHashrate);
    }

    @Test(expected = AccountRequestorException.class)
    public void testApiError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"status\":\"ERROR\",\n" +
                "  \"error\":\"Invalid address\"\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        Utils.testApiError(ETHERMINE, httpClient, ETC, WALLET_ADDRESS, "Invalid address");
    }

    @Test(expected = AccountRequestorException.class)
    public void testNoDataError() throws AccountRequestorException {
        JSONObject response = new JSONObject("{  \n" +
                "  \"status\":\"OK\",\n" +
                "  \"data\":\"NO DATA\"\n" +
                "}");
        OkHttpClient httpClient = Utils.getHttpClient(response.toString(), 200);
        Utils.testApiError(ETHERMINE, httpClient, ETC, WALLET_ADDRESS, "NO DATA");
    }

    @Test(expected = AccountRequestorException.class)
    public void testInternalServerError() throws AccountRequestorException {
        Utils.testInternalServerError(ETHERMINE, ETC, WALLET_ADDRESS);
    }

    @Test(expected = AccountRequestorException.class)
    public void testEmptyResponse() throws AccountRequestorException {
        Utils.testEmptyResponse(ETHERMINE, ETC, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedCoin() throws AccountRequestorException {
        Utils.testUnsupportedCoin(ETHERMINE, BTC, WALLET_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(ETHERMINE, ETC, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWalletAddress() throws AccountRequestorException {
        Utils.testEmptyWalletAddress(ETHERMINE, ETC, null);
    }

}
