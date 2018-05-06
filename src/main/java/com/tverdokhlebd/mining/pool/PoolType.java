package com.tverdokhlebd.mining.pool;

import static com.tverdokhlebd.mining.coin.CoinType.ETH;

import java.util.Arrays;
import java.util.List;

import com.tverdokhlebd.mining.coin.CoinType;

/**
 * Enumerations of pool types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum PoolType {

    DWARFPOOL(Arrays.asList(ETH));

    /** Supported list of coin types. */
    private final List<CoinType> coinTypeList;

    /**
     * Creates instance.
     *
     * @param coinTypeList supported list of coin types
     */
    private PoolType(List<CoinType> coinTypeList) {
        this.coinTypeList = coinTypeList;
    }

    /**
     * Gets coin type list.
     *
     * @return coin type list
     */
    public List<CoinType> getCoinTypeList() {
        return coinTypeList;
    }

}
