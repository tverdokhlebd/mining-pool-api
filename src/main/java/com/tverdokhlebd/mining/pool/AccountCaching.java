package com.tverdokhlebd.mining.pool;

import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.Map;

import com.tverdokhlebd.mining.commons.coin.CoinType;

/**
 * Interface for pool account caching.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public interface AccountCaching {

    /**
     * Gets cached pool account map.
     *
     * @return cached pool account map
     */
    Map<SimpleEntry<CoinType, String>, SimpleEntry<Account, Date>> getCachedAccountMap();

}
