package com.tverdokhlebd.mining.pool;

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

    DWARFPOOL("Dwarfpool", "https://dwarfpool.com", Arrays.asList(CoinType.ETH));

    /** Pool name. */
    private final String name;
    /** Pool official site. */
    private final String website;
    /** Supported list of coin types. */
    private final List<CoinType> coinTypeList;

    /**
     * Creates instance.
     *
     * @param name name of pool
     * @param website official site of pool
     * @param coinTypeList supported list of coin types
     */
    private PoolType(String name, String website, List<CoinType> coinTypeList) {
        this.name = name;
        this.website = website;
        this.coinTypeList = coinTypeList;
    }

    /**
     * Gets name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets website.
     *
     * @return website
     */
    public String getWebsite() {
        return website;
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
