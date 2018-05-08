package com.tverdokhlebd.mining.pool.nanopool;

import static com.tverdokhlebd.mining.commons.coin.CoinType.ETC;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ETH;
import static com.tverdokhlebd.mining.commons.coin.CoinType.XMR;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ZEC;
import static com.tverdokhlebd.mining.commons.http.ErrorCode.API_ERROR;
import static com.tverdokhlebd.mining.commons.http.ErrorCode.PARSE_ERROR;
import static com.tverdokhlebd.mining.pool.PoolType.NANOPOOL;
import static com.tverdokhlebd.mining.commons.utils.TaskUtils.startRepeatedTask;
import static com.tverdokhlebd.mining.commons.utils.TimeUtils.REPEATED_TASK_PERIOD;

import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.tverdokhlebd.mining.commons.coin.CoinType;
import com.tverdokhlebd.mining.commons.http.RequestException;
import com.tverdokhlebd.mining.pool.Account;
import com.tverdokhlebd.mining.pool.Account.Builder;
import com.tverdokhlebd.mining.pool.PoolType;
import com.tverdokhlebd.mining.pool.ethermine.EthermineAccountRequestor;
import com.tverdokhlebd.mining.pool.requestor.AccountBaseRequestor;
import com.tverdokhlebd.mining.commons.utils.HashrateUtils;

import okhttp3.OkHttpClient;

/**
 * Nanopool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class NanopoolAccountRequestor extends AccountBaseRequestor {

    /** Request name of balance. */
    private static final String BALANCE_REQUEST_NAME = "BALANCE";
    /** Request name of ETH reported hashrate. */
    private static final String ETH_REPORTED_HASHRATE_REQUEST_NAME = "ETH_REPORTED_HASHRATE";
    /** Request name of ETC reported hashrate. */
    private static final String ETC_REPORTED_HASHRATE_REQUEST_NAME = "ETC_REPORTED_HASHRATE";
    /** Request name of XMR reported hashrate. */
    private static final String XMR_REPORTED_HASHRATE_REQUEST_NAME = "XMR_REPORTED_HASHRATE";
    /** Request name of ZEC reported hashrate. */
    private static final String ZEC_REPORTED_HASHRATE_REQUEST_NAME = "ZEC_REPORTED_HASHRATE";
    /** Map of urls. */
    private static final Map<CoinType, List<SimpleEntry<String, String>>> URL_MAP = new HashMap<>();
    /** Fills map of urls. */
    static {
        List<SimpleEntry<String, String>> ethUrlList = new ArrayList<>();
        ethUrlList.add(new SimpleEntry<String, String>(BALANCE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/eth/balance/" + WALLET_ADDRESS_PATTERN));
        ethUrlList.add(new SimpleEntry<String, String>(ETH_REPORTED_HASHRATE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/eth/reportedhashrate/" + WALLET_ADDRESS_PATTERN));
        URL_MAP.put(ETH, ethUrlList);
        List<SimpleEntry<String, String>> etcUrlList = new ArrayList<>();
        etcUrlList.add(new SimpleEntry<String, String>(BALANCE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/etc/balance/" + WALLET_ADDRESS_PATTERN));
        etcUrlList.add(new SimpleEntry<String, String>(ETC_REPORTED_HASHRATE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/etc/reportedhashrate/" + WALLET_ADDRESS_PATTERN));
        URL_MAP.put(ETC, etcUrlList);
        List<SimpleEntry<String, String>> xmrUrlList = new ArrayList<>();
        xmrUrlList.add(new SimpleEntry<String, String>(BALANCE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/xmr/balance/" + WALLET_ADDRESS_PATTERN));
        xmrUrlList.add(new SimpleEntry<String, String>(XMR_REPORTED_HASHRATE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/xmr/reportedhashrate/" + WALLET_ADDRESS_PATTERN));
        URL_MAP.put(XMR, xmrUrlList);
        List<SimpleEntry<String, String>> zecUrlList = new ArrayList<>();
        zecUrlList.add(new SimpleEntry<String, String>(BALANCE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/zec/balance/" + WALLET_ADDRESS_PATTERN));
        zecUrlList.add(new SimpleEntry<String, String>(ZEC_REPORTED_HASHRATE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/zec/reportedhashrate/" + WALLET_ADDRESS_PATTERN));
        URL_MAP.put(ZEC, zecUrlList);
    }
    /** Cached accounts. */
    private static final Map<SimpleEntry<CoinType, String>, SimpleEntry<Account, Date>> ACCOUNT_MAP = new ConcurrentHashMap<>();
    /** Initializes repeated task for removing cached accounts. */
    static {
        startRepeatedTask(EthermineAccountRequestor.class.getName(), new TimerTask() {

            @Override
            public void run() {
                ACCOUNT_MAP.entrySet().removeIf(t -> new Date().after(t.getValue().getValue()));
            }
        }, REPEATED_TASK_PERIOD);
    }

    /**
     * Creates instance.
     *
     * @param httpClient HTTP client
     * @param useAccountCaching use accounts caching or not
     * @param accountCachingTimeInMinutes amount of minutes for account caching
     */
    public NanopoolAccountRequestor(OkHttpClient httpClient, boolean useAccountCaching, int accountCachingTimeInMinutes) {
        super(httpClient, useAccountCaching, accountCachingTimeInMinutes);
    }

    @Override
    public Map<SimpleEntry<CoinType, String>, SimpleEntry<Account, Date>> getCachedAccountMap() {
        return ACCOUNT_MAP;
    }

    @Override
    protected PoolType getPoolType() {
        return NANOPOOL;
    }

    @Override
    protected List<SimpleEntry<String, String>> getUrlList(CoinType coinType) {
        return URL_MAP.get(coinType);
    }

    @Override
    protected void checkApiError(String responseBody, String requestName) throws RequestException {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            boolean status = jsonResponse.getBoolean("status");
            if (!status) {
                String errorMessage = jsonResponse.getString("error");
                throw new RequestException(API_ERROR, errorMessage);
            }
        } catch (JSONException e) {
            throw new RequestException(PARSE_ERROR, e);
        }
    }

    @Override
    protected void parseResponse(String responseBody, String requestName, Builder result) throws RequestException {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            BigDecimal data = BigDecimal.valueOf(jsonResponse.getDouble("data"));
            if (requestName.equals(BALANCE_REQUEST_NAME)) {
                result.setWalletBalance(data);
            }
            switch (requestName) {
            case ETH_REPORTED_HASHRATE_REQUEST_NAME:
            case ETC_REPORTED_HASHRATE_REQUEST_NAME: {
                data = HashrateUtils.convertMegaHashesToHashes(data);
                result.setReportedHashrate(data);
                break;
            }
            case XMR_REPORTED_HASHRATE_REQUEST_NAME:
            case ZEC_REPORTED_HASHRATE_REQUEST_NAME: {
                result.setReportedHashrate(data);
                break;
            }
            }
        } catch (JSONException e) {
            throw new RequestException(PARSE_ERROR, e);
        }
    }

}
