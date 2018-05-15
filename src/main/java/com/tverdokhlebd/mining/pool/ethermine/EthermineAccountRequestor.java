package com.tverdokhlebd.mining.pool.ethermine;

import static com.tverdokhlebd.mining.commons.http.ErrorCode.API_ERROR;
import static com.tverdokhlebd.mining.commons.http.ErrorCode.PARSE_ERROR;
import static com.tverdokhlebd.mining.commons.utils.TaskUtils.startRepeatedTask;
import static com.tverdokhlebd.mining.commons.utils.TimeUtils.REPEATED_TASK_PERIOD;
import static com.tverdokhlebd.mining.pool.PoolType.ETHERMINE;
import static com.tverdokhlebd.mining.pool.ethermine.UrlList.URL_MAP;

import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
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
import com.tverdokhlebd.mining.pool.requestor.AccountBaseRequestor;

import okhttp3.OkHttpClient;

/**
 * Ethermine account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class EthermineAccountRequestor extends AccountBaseRequestor {

    /** Ether value. */
    private static final BigDecimal ETHER = BigDecimal.valueOf(1_000_000_000_000_000_000L);
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
    public EthermineAccountRequestor(OkHttpClient httpClient, boolean useAccountCaching, int accountCachingTimeInMinutes) {
        super(httpClient, useAccountCaching, accountCachingTimeInMinutes);
    }

    @Override
    public Map<SimpleEntry<CoinType, String>, SimpleEntry<Account, Date>> getCachedAccountMap() {
        return ACCOUNT_MAP;
    }

    @Override
    protected PoolType getPoolType() {
        return ETHERMINE;
    }

    @Override
    protected List<SimpleEntry<String, String>> getUrlList(CoinType coinType) {
        return URL_MAP.get(coinType);
    }

    @Override
    protected void checkApiError(String responseBody, String requestName) throws RequestException {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            String status = jsonResponse.getString("status");
            if (status.equalsIgnoreCase("error")) {
                String errorMessage = jsonResponse.getString("error");
                throw new RequestException(API_ERROR, errorMessage);
            }
            try {
                String data = jsonResponse.getString("data");
                throw new RequestException(API_ERROR, data);
            } catch (JSONException e) {
                // Skip
            }
        } catch (JSONException e) {
            throw new RequestException(PARSE_ERROR, e);
        }
    }

    @Override
    protected void parseResponse(String responseBody, String requestName, Builder result) throws RequestException {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONObject data = jsonResponse.getJSONObject("data");
            BigDecimal walletBalance = BigDecimal.valueOf(data.getLong("unpaid"));
            result.setWalletBalance(walletBalance.divide(ETHER));
            BigDecimal reportedHashrate = BigDecimal.valueOf(data.getDouble("reportedHashrate"));
            result.setReportedHashrate(reportedHashrate);
        } catch (JSONException e) {
            throw new RequestException(PARSE_ERROR, e);
        }
    }

}
