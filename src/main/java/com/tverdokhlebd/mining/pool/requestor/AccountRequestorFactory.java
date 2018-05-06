package com.tverdokhlebd.mining.pool.requestor;

import com.tverdokhlebd.mining.http.HttpClientFactory;
import com.tverdokhlebd.mining.pool.PoolType;
import com.tverdokhlebd.mining.pool.dwarfpool.DwarfpoolAccountRequestor;
import com.tverdokhlebd.mining.pool.ethermine.EthermineAccountRequestor;
import com.tverdokhlebd.mining.pool.nanopool.NanopoolAccountRequestor;

import okhttp3.OkHttpClient;

/**
 * Factory for creating pool account requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class AccountRequestorFactory {

    /** Default amount of minutes for account caching. */
    private static final int DEFAULT_ACCOUNT_CACHING_TIME_IN_MINUTES = 2;

    /**
     * Creates pool account requestor.
     *
     * @param poolType pool type
     * @return pool account requestor
     */
    public static AccountRequestor create(PoolType poolType) {
        OkHttpClient httpClient = HttpClientFactory.create();
        return create(poolType, httpClient);
    }

    /**
     * Creates pool account requestor.
     *
     * @param poolType pool type
     * @param httpClient HTTP client
     * @return pool account requestor
     */
    public static AccountRequestor create(PoolType poolType, OkHttpClient httpClient) {
        return create(poolType, httpClient, true);
    }

    /**
     * Creates pool account requestor.
     *
     * @param poolType pool type
     * @param httpClient HTTP client
     * @param useAccountCaching use accounts caching or not
     * @return pool account requestor
     */
    public static AccountRequestor create(PoolType poolType, OkHttpClient httpClient, boolean useAccountCaching) {
        return create(poolType, httpClient, useAccountCaching, DEFAULT_ACCOUNT_CACHING_TIME_IN_MINUTES);
    }

    /**
     * Creates pool account requestor.
     *
     * @param poolType pool type
     * @param httpClient HTTP client
     * @param useAccountCaching use accounts caching or not
     * @param accountCachingTimeInMinutes amount of minutes for account caching
     * @return pool account requestor
     */
    public static AccountRequestor create(PoolType poolType, OkHttpClient httpClient, boolean useAccountCaching,
            int accountCachingTimeInMinutes) {
        switch (poolType) {
        case DWARFPOOL: {
            return new DwarfpoolAccountRequestor(httpClient, useAccountCaching, accountCachingTimeInMinutes);
        }
        case ETHERMINE: {
            return new EthermineAccountRequestor(httpClient, useAccountCaching, accountCachingTimeInMinutes);
        }
        case NANOPOOL: {
            return new NanopoolAccountRequestor(httpClient, useAccountCaching, accountCachingTimeInMinutes);
        }
        default:
            throw new IllegalArgumentException(poolType.name());
        }
    }

}
