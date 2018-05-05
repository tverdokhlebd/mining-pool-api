package com.tverdokhlebd.mining.coin;

/**
 * Enumerations of coin types.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public enum CoinType {

    ETH("Ethereum", "ETH", "https://www.ethereum.org");

    /** Coin name. */
    private final String name;
    /** Coin symbol. */
    private final String symbol;
    /** Coin official site. */
    private final String website;

    /**
     * Creates instance.
     *
     * @param name name of coin
     * @param symbol symbol of coin
     * @param website official site of website
     */
    private CoinType(String name, String symbol, String website) {
        this.name = name;
        this.symbol = symbol;
        this.website = website;
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
     * Gets symbol.
     *
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets website.
     *
     * @return website
     */
    public String getWebsite() {
        return website;
    }

}
