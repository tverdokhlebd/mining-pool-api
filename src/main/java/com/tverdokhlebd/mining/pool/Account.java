package com.tverdokhlebd.mining.pool;

import java.math.BigDecimal;

/**
 * Class for representing pool account.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class Account {

    /** Wallet address. */
    private final String walletAddress;
    /** Wallet balance. */
    private final BigDecimal walletBalance;
    /** Reported hashrate in H/s. */
    private final BigDecimal reportedHashrate;

    /**
     * Creates the instance.
     *
     * @param walletAddress wallet address
     * @param walletBalance wallet balance
     * @param reportedHashrate reported hashrate in H/s
     */
    private Account(String walletAddress, BigDecimal walletBalance, BigDecimal reportedHashrate) {
        super();
        this.walletAddress = walletAddress;
        this.walletBalance = walletBalance.stripTrailingZeros();
        this.reportedHashrate = reportedHashrate.stripTrailingZeros();
    }

    /**
     * Gets wallet address.
     *
     * @return wallet address
     */
    public String getWalletAddress() {
        return walletAddress;
    }

    /**
     * Gets wallet balance.
     *
     * @return wallet balance
     */
    public BigDecimal getWalletBalance() {
        return walletBalance;
    }

    /**
     * Gets reported hashrate.
     *
     * @return reported hashrate
     */
    public BigDecimal getReportedHashrate() {
        return reportedHashrate;
    }

}
