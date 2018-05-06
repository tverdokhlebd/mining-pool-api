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
        this.walletBalance = walletBalance;
        this.reportedHashrate = reportedHashrate;
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

    /**
     * Builder of pool account.
     */
    public static class Builder {

        /** Wallet address. */
        private String walletAddress;
        /** Wallet balance. */
        private BigDecimal walletBalance;
        /** Reported hashrate in H/s. */
        private BigDecimal reportedHashrate;

        /**
         * Creates instance.
         */
        public Builder() {
            super();
        }

        /**
         * Sets wallet address.
         *
         * @param walletAddress new wallet address
         */
        public void setWalletAddress(String walletAddress) {
            this.walletAddress = walletAddress;
        }

        /**
         * Sets wallet balance.
         *
         * @param walletBalance new wallet balance
         */
        public void setWalletBalance(BigDecimal walletBalance) {
            this.walletBalance = walletBalance;
        }

        /**
         * Sets reported hashrate.
         *
         * @param reportedHashrate new reported hashrate
         */
        public void setReportedHashrate(BigDecimal reportedHashrate) {
            this.reportedHashrate = reportedHashrate;
        }

        /**
         * Builds pool account.
         *
         * @return pool account
         */
        public Account build() {
            return new Account(walletAddress, walletBalance, reportedHashrate);
        }

    }

}
