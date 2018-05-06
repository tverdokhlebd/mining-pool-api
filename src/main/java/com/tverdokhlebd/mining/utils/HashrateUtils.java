package com.tverdokhlebd.mining.utils;

import static java.math.RoundingMode.DOWN;

import java.math.BigDecimal;

/**
 * Utils for working with hashrate.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class HashrateUtils {

    /** 1 kH/s is 1,000 hashes per second. */
    private static final BigDecimal KH_S = BigDecimal.valueOf(1_000);
    /** 1 MH/s is 1,000,000 hashes per second. */
    private static final BigDecimal MH_S = BigDecimal.valueOf(1_000_000);

    /**
     * Converts kilohashes to hashes.
     *
     * @param kiloHashes value in kilohashes
     * @return result in hashes
     */
    public static BigDecimal convertKiloHashesToHashes(BigDecimal kiloHashes) {
        return kiloHashes.multiply(KH_S).setScale(0, DOWN);
    }

    /**
     * Converts megahashes to hashes.
     *
     * @param megaHashes value in megahashes
     * @return result in hashes
     */
    public static BigDecimal convertMegaHashesToHashes(BigDecimal megaHashes) {
        return megaHashes.multiply(MH_S).setScale(0, DOWN);
    }

}
