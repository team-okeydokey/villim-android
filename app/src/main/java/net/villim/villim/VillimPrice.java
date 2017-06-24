package net.villim.villim;

import java.util.Date;

/**
 * Created by seongmin on 6/24/17.
 */

public class VillimPrice {

    public Date startDate;
    public Date endDate;

    public int totalPrice;
    public int basePrice;
    public int utilityFee;
    public int cleaningFee;

    public VillimPrice(Date start, Date end) {
        this.startDate = start;
        this.endDate = end;

        initializePrices();
    }

    private void initializePrices() {
        totalPrice = 5000;
        basePrice = 4000;
        utilityFee = 300;
        cleaningFee = 2;
    }
}
