package com.tverdokhlebd.mining.pool;

import static com.tverdokhlebd.mining.commons.coin.CoinType.ETC;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ETH;
import static com.tverdokhlebd.mining.commons.coin.CoinType.XMR;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ZEC;

import java.util.Arrays;
import java.util.List;

import com.tverdokhlebd.mining.commons.coin.CoinType;

/**
 * Enumerations of pool types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum PoolType {

    DWARFPOOL(Arrays.asList(ETH, XMR, ZEC)),
    ETHERMINE(Arrays.asList(ETH, ETC, ZEC)),
    NANOPOOL(Arrays.asList(ETH, ETC, XMR, ZEC));

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
