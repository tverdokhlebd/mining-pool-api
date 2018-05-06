package com.tverdokhlebd.mining.pool.dwarfpool;

import static com.tverdokhlebd.mining.coin.CoinType.ETH;
import static com.tverdokhlebd.mining.http.ErrorCode.API_ERROR;
import static com.tverdokhlebd.mining.http.ErrorCode.PARSE_ERROR;
import static com.tverdokhlebd.mining.pool.PoolType.DWARFPOOL;
import static com.tverdokhlebd.mining.utils.TaskUtils.startRepeatedTask;
import static java.util.concurrent.TimeUnit.SECONDS;

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

import com.tverdokhlebd.mining.coin.CoinType;
import com.tverdokhlebd.mining.http.RequestException;
import com.tverdokhlebd.mining.pool.Account;
import com.tverdokhlebd.mining.pool.Account.Builder;
import com.tverdokhlebd.mining.pool.AccountBaseRequestor;
import com.tverdokhlebd.mining.pool.PoolType;

import okhttp3.OkHttpClient;

/**
 * Dwarfpool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class DwarfpoolAccountRequestor extends AccountBaseRequestor {

    /** Request name of common data. */
    private static final String COMMON_DATA_REQUEST_NAME = "COMMON_DATA";
    /** Map of urls. */
    private static final Map<CoinType, List<SimpleEntry<String, String>>> URL_MAP = new HashMap<>();
    /** Fills map of urls. */
    static {
        List<SimpleEntry<String, String>> ethUrlList = new ArrayList<>();
        ethUrlList.add(new SimpleEntry<String, String>(COMMON_DATA_REQUEST_NAME,
                                                       "http://dwarfpool.com/eth/api?wallet=" + WALLET_ADDRESS_PATTERN));
        URL_MAP.put(ETH, ethUrlList);
    }
    /** Cached accounts. */
    private static final Map<SimpleEntry<CoinType, String>, SimpleEntry<Account, Date>> ACCOUNT_MAP = new ConcurrentHashMap<>();
    /** Initializes repeated task for removing cached accounts. */
    static {
        startRepeatedTask(DwarfpoolAccountRequestor.class.getName(), new TimerTask() {

            @Override
            public void run() {
                ACCOUNT_MAP.entrySet().removeIf(t -> new Date().after(t.getValue().getValue()));
            }
        }, SECONDS.toMillis(10));
    }

    /**
     * Creates instance.
     *
     * @param httpClient HTTP client
     * @param useAccountCaching use accounts caching or not
     * @param accountCachingTimeInMinutes amount of minutes for account caching
     */
    public DwarfpoolAccountRequestor(OkHttpClient httpClient, boolean useAccountCaching, int accountCachingTimeInMinutes) {
        super(httpClient, useAccountCaching, accountCachingTimeInMinutes);
    }

    @Override
    public Map<SimpleEntry<CoinType, String>, SimpleEntry<Account, Date>> getCachedAccountMap() {
        return ACCOUNT_MAP;
    }

    @Override
    protected PoolType getPoolType() {
        return DWARFPOOL;
    }

    @Override
    protected List<SimpleEntry<String, String>> getUrlList(CoinType coinType) {
        return URL_MAP.get(coinType);
    }

    @Override
    protected void checkApiError(String responseBody, String requestName) throws RequestException {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            boolean error = jsonResponse.getBoolean("error");
            if (error) {
                String errorCode = jsonResponse.getString("error_code");
                throw new RequestException(API_ERROR, errorCode);
            }
        } catch (JSONException e) {
            throw new RequestException(PARSE_ERROR, e);
        }
    }

    @Override
    protected void parseResponse(String responseBody, String requestName, Builder result) throws RequestException {
        try {
            if (requestName.equals(COMMON_DATA_REQUEST_NAME)) {
                JSONObject jsonResponse = new JSONObject(responseBody);
                result.setWalletBalance(BigDecimal.valueOf(jsonResponse.getDouble("wallet_balance")));
                result.setReportedHashrate(BigDecimal.valueOf(jsonResponse.getDouble("total_hashrate")));
            }
        } catch (JSONException e) {
            throw new RequestException(PARSE_ERROR, e);
        }
    }

}
