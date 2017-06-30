package net.villim.villim;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by seongmin on 6/24/17.
 */

public class VillimPrice {

    private static final float TWENTY = 0.2f;
    private static final float FIFTEEN = 0.15f;

    public Date startDate;
    public Date endDate;

    public int monthlyRent;
    public float dailyRent;
    public int totalPrice;
    public int basePrice;
    public int utilityFee;
    public int cleaningFee;

    public VillimPrice(Date start, Date end, int rent, int cleaningFee) {
        this.startDate = start;
        this.endDate = end;
        this.monthlyRent = rent;
        this.dailyRent = (float) rent / 30.0f;
        this.cleaningFee = cleaningFee;

        initializePrices();
    }

    private void initializePrices() {
        int total = 0;
        int base = 0;
        float util = 0;

        /* Start counting from start date.s */
        Calendar currCal = Calendar.getInstance();
        currCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        Calendar nextCal = Calendar.getInstance();
        nextCal.setTime(currCal.getTime());
        nextCal.add(Calendar.MONTH, 1);

        /* Count until we go over end date */
        while (nextCal.before(endCal)) {

            nextCal.setTime(currCal.getTime());
            nextCal.add(Calendar.MONTH, 1);

            /* Base price */
            base += monthlyRent;

            /* Utility fees */
            float currUtilityRate = isLowUtility(currCal.getTime()) ? FIFTEEN : TWENTY;
            float nextUtilityRate = isLowUtility(nextCal.getTime()) ? FIFTEEN : TWENTY;

            if (currUtilityRate != nextUtilityRate) {

                float currUtilityFee = VillimUtil.daysUntilEndOfMonth(currCal.getTime()) * dailyRent * currUtilityRate;
                float nextUtilityFee = VillimUtil.daysFromStartOfMonth(currCal.getTime()) * dailyRent * nextUtilityRate;

                util += currUtilityFee;
                util += nextUtilityFee;

            } else {
                util += monthlyRent * currUtilityRate;
            }

            /* Go to next month */
            currCal.add(Calendar.MONTH, 1);
        }

//        /* Go back to last month */
//        currCal.add(Calendar.MONTH, -1);
//        base -= monthlyRent;

        /* Calculate daily rent */
        int days = VillimUtil.daysBetween(currCal.getTime(), endDate);
        if (days > 0) {
            base += days * dailyRent;
        }

        /* Store calculations */
        this.basePrice = base;
        this.utilityFee = (int) util;
        this.totalPrice = this.basePrice + this.utilityFee + this.cleaningFee;
    }

    /* Returns true for 3월 ~ 5월 && 9월 && 11월.
     * Months are 0 based!!! */
    private boolean isLowUtility(Date date) {
        return date.getMonth() == 2 ||
                date.getMonth() == 3 ||
                date.getMonth() == 4 ||
                date.getMonth() == 8 ||
                date.getMonth() == 9 ||
                date.getMonth() == 10;
    }
}
