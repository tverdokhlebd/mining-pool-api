package com.tverdokhlebd.mining.pool.requestor;

import static com.tverdokhlebd.mining.commons.utils.TimeUtils.addMinutes;

import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.List;

import com.tverdokhlebd.mining.commons.coin.CoinType;
import com.tverdokhlebd.mining.commons.http.BaseRequestor;
import com.tverdokhlebd.mining.commons.http.RequestException;
import com.tverdokhlebd.mining.pool.Account;
import com.tverdokhlebd.mining.pool.AccountCaching;
import com.tverdokhlebd.mining.pool.PoolType;

import okhttp3.OkHttpClient;

/**
 * Pool account base HTTP requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public abstract class AccountBaseRequestor extends BaseRequestor<Account.Builder> implements AccountRequestor, AccountCaching {

    /** Use accounts caching or not. */
    private final boolean useAccountCaching;
    /** Amount of minutes for account caching. */
    private final int accountCachingTimeInMinutes;
    /** Wallet address pattern for replacing in URL. */
    public static final String WALLET_ADDRESS_PATTERN = "%WALLET_ADDRESS%";

    /**
     * Creates instance.
     *
     * @param httpClient HTTP client
     * @param useAccountCaching use accounts caching or not
     * @param accountCachingTimeInMinutes amount of minutes for account caching
     */
    protected AccountBaseRequestor(OkHttpClient httpClient, boolean useAccountCaching, int accountCachingTimeInMinutes) {
        super(httpClient);
        this.useAccountCaching = useAccountCaching;
        this.accountCachingTimeInMinutes = accountCachingTimeInMinutes;
    }

    @Override
    public Account requestAccount(CoinType coinType, String walletAddress) throws AccountRequestorException {
        if (getPoolType().getCoinTypeList().indexOf(coinType) == -1) {
            throw new IllegalArgumentException(coinType.name() + " is not supported");
        }
        if (walletAddress == null || walletAddress.isEmpty()) {
            throw new IllegalArgumentException("Wallet address is null or empty");
        }
        try {
            if (useAccountCaching) {
                SimpleEntry<CoinType, String> cachedKey = new SimpleEntry<CoinType, String>(coinType, walletAddress);
                SimpleEntry<Account, Date> cachedAccount = getCachedAccountMap().get(cachedKey);
                if (cachedAccount == null) {
                    Account newAccount = requestAccountFromPool(coinType, walletAddress);
                    cachedAccount = new SimpleEntry<Account, Date>(newAccount, addMinutes(new Date(), accountCachingTimeInMinutes));
                    getCachedAccountMap().put(cachedKey, cachedAccount);
                }
                return cachedAccount.getKey();
            } else {
                return requestAccountFromPool(coinType, walletAddress);
            }
        } catch (RequestException e) {
            throw new AccountRequestorException(e);
        }
    }

    /**
     * Requests pool account from pool service.
     *
     * @param coinType type of coin
     * @param walletAddress wallet address
     * @return pool account
     * @throws RequestException if there is any error in making request
     */
    private Account requestAccountFromPool(CoinType coinType, String walletAddress) throws RequestException {
        Account.Builder accountBuilder = new Account.Builder();
        accountBuilder.setWalletAddress(walletAddress);
        List<SimpleEntry<String, String>> urlList = getUrlList(coinType);
        for (int i = 0; i < urlList.size(); i++) {
            SimpleEntry<String, String> urlEntry = urlList.get(i);
            String requestName = urlEntry.getKey();
            String preparedUrl = urlEntry.getValue().replaceAll(WALLET_ADDRESS_PATTERN, walletAddress);
            super.request(preparedUrl, requestName, accountBuilder);
        }
        return accountBuilder.build();
    }

    /**
     * Gets pool type.
     *
     * @return pool type
     */
    protected abstract PoolType getPoolType();

    /**
     * Gets list of urls.
     *
     * @param coinType type of coin
     * @return list of urls
     */
    protected abstract List<SimpleEntry<String, String>> getUrlList(CoinType coinType);

}
