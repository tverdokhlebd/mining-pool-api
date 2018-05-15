package com.tverdokhlebd.mining.pool.ethermine;

import static com.tverdokhlebd.mining.commons.coin.CoinType.ETC;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ETH;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ZEC;
import static com.tverdokhlebd.mining.pool.requestor.AccountBaseRequestor.WALLET_ADDRESS_PATTERN;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tverdokhlebd.mining.commons.coin.CoinType;

/**
 * List of urls for requestor.
 *
 * @author Dmitry Tverdokhleb
 *
 */
class UrlList {

    /** Map of urls. */
    static final Map<CoinType, List<SimpleEntry<String, String>>> URL_MAP = new HashMap<>();
    /** Request name of ETH common data. */
    static final String ETH_COMMON_DATA_REQUEST_NAME = "ETH_COMMON_DATA";
    /** Request name of ETC common data. */
    static final String ETC_COMMON_DATA_REQUEST_NAME = "ETC_COMMON_DATA";
    /** Request name of ZEC common data. */
    static final String ZEC_COMMON_DATA_REQUEST_NAME = "ZEC_COMMON_DATA";
    /** Fills map of urls. */
    static {
        List<SimpleEntry<String, String>> ethUrlList = new ArrayList<>();
        ethUrlList.add(new SimpleEntry<String, String>(ETH_COMMON_DATA_REQUEST_NAME,
                                                       "https://api.ethermine.org/miner/" + WALLET_ADDRESS_PATTERN + "/currentStats"));
        URL_MAP.put(ETH, ethUrlList);
        List<SimpleEntry<String, String>> etcUrlList = new ArrayList<>();
        etcUrlList.add(new SimpleEntry<String, String>(ETC_COMMON_DATA_REQUEST_NAME,
                                                       "https://api-etc.ethermine.org/miner/" + WALLET_ADDRESS_PATTERN + "/currentStats"));
        URL_MAP.put(ETC, etcUrlList);
        List<SimpleEntry<String, String>> zecUrlList = new ArrayList<>();
        zecUrlList.add(new SimpleEntry<String, String>(ZEC_COMMON_DATA_REQUEST_NAME,
                                                       "https://api-zcash.flypool.org/miner/" + WALLET_ADDRESS_PATTERN + "/currentStats"));
        URL_MAP.put(ZEC, zecUrlList);
    }

}
