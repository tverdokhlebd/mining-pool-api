package com.tverdokhlebd.mining.pool;

import com.tverdokhlebd.mining.coin.CoinType;

/**
 * Interface for requesting pool account.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface AccountRequestor {

    /**
     * Requests pool account.
     *
     * @param coinType type of coin
     * @param walletAddress wallet address
     * @return pool account
     * @throws AccountRequestorException if there is any error in account requesting
     */
    Account requestAccount(CoinType coinType, String walletAddress) throws AccountRequestorException;

}