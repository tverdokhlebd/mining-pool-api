package com.tverdokhlebd.mining.pool.nanopool;

import static com.tverdokhlebd.mining.commons.coin.CoinType.ETC;
import static com.tverdokhlebd.mining.commons.coin.CoinType.ETH;
import static com.tverdokhlebd.mining.commons.coin.CoinType.XMR;
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

    /** Request name of balance. */
    static final String BALANCE_REQUEST_NAME = "BALANCE";
    /** Request name of ETH reported hashrate. */
    static final String ETH_REPORTED_HASHRATE_REQUEST_NAME = "ETH_REPORTED_HASHRATE";
    /** Request name of ETC reported hashrate. */
    static final String ETC_REPORTED_HASHRATE_REQUEST_NAME = "ETC_REPORTED_HASHRATE";
    /** Request name of XMR reported hashrate. */
    static final String XMR_REPORTED_HASHRATE_REQUEST_NAME = "XMR_REPORTED_HASHRATE";
    /** Request name of ZEC reported hashrate. */
    static final String ZEC_REPORTED_HASHRATE_REQUEST_NAME = "ZEC_REPORTED_HASHRATE";
    /** Map of urls. */
    static final Map<CoinType, List<SimpleEntry<String, String>>> URL_MAP = new HashMap<>();
    /** Fills map of urls. */
    static {
        List<SimpleEntry<String, String>> ethUrlList = new ArrayList<>();
        ethUrlList.add(new SimpleEntry<String, String>(BALANCE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/eth/balance/" + WALLET_ADDRESS_PATTERN));
        ethUrlList.add(new SimpleEntry<String, String>(ETH_REPORTED_HASHRATE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/eth/reportedhashrate/" + WALLET_ADDRESS_PATTERN));
        URL_MAP.put(ETH, ethUrlList);
        List<SimpleEntry<String, String>> etcUrlList = new ArrayList<>();
        etcUrlList.add(new SimpleEntry<String, String>(BALANCE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/etc/balance/" + WALLET_ADDRESS_PATTERN));
        etcUrlList.add(new SimpleEntry<String, String>(ETC_REPORTED_HASHRATE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/etc/reportedhashrate/" + WALLET_ADDRESS_PATTERN));
        URL_MAP.put(ETC, etcUrlList);
        List<SimpleEntry<String, String>> xmrUrlList = new ArrayList<>();
        xmrUrlList.add(new SimpleEntry<String, String>(BALANCE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/xmr/balance/" + WALLET_ADDRESS_PATTERN));
        xmrUrlList.add(new SimpleEntry<String, String>(XMR_REPORTED_HASHRATE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/xmr/reportedhashrate/" + WALLET_ADDRESS_PATTERN));
        URL_MAP.put(XMR, xmrUrlList);
        List<SimpleEntry<String, String>> zecUrlList = new ArrayList<>();
        zecUrlList.add(new SimpleEntry<String, String>(BALANCE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/zec/balance/" + WALLET_ADDRESS_PATTERN));
        zecUrlList.add(new SimpleEntry<String, String>(ZEC_REPORTED_HASHRATE_REQUEST_NAME,
                                                       "https://api.nanopool.org/v1/zec/reportedhashrate/" + WALLET_ADDRESS_PATTERN));
        URL_MAP.put(ZEC, zecUrlList);
    }

}
