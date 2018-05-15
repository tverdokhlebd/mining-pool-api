package com.tverdokhlebd.mining.pool.dwarfpool;

import static com.tverdokhlebd.mining.commons.http.ErrorCode.API_ERROR;
import static com.tverdokhlebd.mining.commons.http.ErrorCode.PARSE_ERROR;
import static com.tverdokhlebd.mining.commons.utils.TaskUtils.startRepeatedTask;
import static com.tverdokhlebd.mining.commons.utils.TimeUtils.REPEATED_TASK_PERIOD;
import static com.tverdokhlebd.mining.pool.PoolType.DWARFPOOL;

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
import com.tverdokhlebd.mining.commons.utils.HashrateUtils;
import com.tverdokhlebd.mining.pool.Account;
import com.tverdokhlebd.mining.pool.Account.Builder;
import com.tverdokhlebd.mining.pool.PoolType;
import com.tverdokhlebd.mining.pool.requestor.AccountBaseRequestor;

import okhttp3.OkHttpClient;

/**
 * Dwarfpool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class DwarfpoolAccountRequestor extends AccountBaseRequestor {

    /** Cached accounts. */
    private static final Map<SimpleEntry<CoinType, String>, SimpleEntry<Account, Date>> ACCOUNT_MAP = new ConcurrentHashMap<>();
    /** Initializes repeated task for removing cached accounts. */
    static {
        startRepeatedTask(DwarfpoolAccountRequestor.class.getName(), new TimerTask() {

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
        return UrlList.URL_MAP.get(coinType);
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
            JSONObject jsonResponse = new JSONObject(responseBody);
            BigDecimal walletBalance = BigDecimal.valueOf(jsonResponse.getDouble("wallet_balance"));
            result.setWalletBalance(walletBalance);
            BigDecimal reportedHashrate = BigDecimal.valueOf(jsonResponse.getDouble("total_hashrate"));
            switch (requestName) {
            case UrlList.ETH_COMMON_DATA_REQUEST_NAME: {
                reportedHashrate = HashrateUtils.convertMegaHashesToHashes(reportedHashrate);
                break;
            }
            case UrlList.XMR_COMMON_DATA_REQUEST_NAME: {
                reportedHashrate = HashrateUtils.convertKiloHashesToHashes(reportedHashrate);
                break;
            }
            }
            result.setReportedHashrate(reportedHashrate);
        } catch (JSONException e) {
            throw new RequestException(PARSE_ERROR, e);
        }
    }

}
