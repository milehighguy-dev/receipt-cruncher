package org.openapitools.service;

import org.openapitools.model.Item;
import org.openapitools.model.Receipt;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


/**
 * These rules collectively define how many points should be awarded to a receipt.
 *
 * One point for every alphanumeric character in the retailer name.
 * 50 points if the total is a round dollar amount with no cents.
 * 25 points if the total is a multiple of 0.25.
 * 5 points for every two items on the receipt.
 * If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer. The result is the number of points earned.
 * 6 points if the day in the purchase date is odd.
 * 10 points if the time of purchase is after 2:00pm and before 4:00pm.
 */
public class ReceiptsScorer {

    public static void score(Receipt receipt) {
        long score = 0;

        score += scoreAlphaNumeric(receipt.getRetailer());

        BigDecimal total = new BigDecimal(0);
        try {
            total = new BigDecimal(receipt.getTotal());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid total format: " + receipt.getTotal(), e);
        }

        if (isRoundDollarAmount(total)) {
            score += 50;
        }

        if (isMultipleOfQuarter(total)) {
            score += 25;
        }

        score += 5L * (receipt.getItems().size() / 2);

        for (Item item : receipt.getItems()) {
            score += calculateItemDescriptionScore(item);
        }

        if (isPurchaseDateOdd(receipt.getPurchaseDate())) {
            score += 6L;
        }

        if (isPurchaseTimeInRange(receipt.getPurchaseTime())) {
            score += 10L;
        }

        receipt.setScore(score);
    }

    private static long scoreAlphaNumeric(String string) {
        long score = 0;
        for (char c : string.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                score++;
            }
        }
        return score;
    }

    private static boolean isRoundDollarAmount(BigDecimal total) {
        // check if there is a decimal
        return total.stripTrailingZeros().scale() <= 0;
    }

    private static boolean isMultipleOfQuarter(BigDecimal total) {
        return total.remainder(BigDecimal.valueOf(0.25)).compareTo(BigDecimal.ZERO) == 0;
    }

    private static long calculateItemDescriptionScore(Item item) {

        BigDecimal price = new BigDecimal(0);
        try {
            price = new BigDecimal(item.getPrice());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price format: " + item.getPrice(), e);
        }

        long score = 0L;
        String description = item.getShortDescription().trim();
        if (description.length() % 3 == 0) {
            score = (long) Math.ceil(
                    price.multiply(BigDecimal.valueOf(0.2)).doubleValue()
                    );
        }
        return score;
    }

    private static boolean isPurchaseDateOdd(LocalDate date) {
        return date.getDayOfMonth() % 2 != 0;
    }

    private static boolean isPurchaseTimeInRange(LocalTime purchaseTime) {

        LocalTime start = LocalTime.of(14, 0);
        LocalTime end = LocalTime.of(16, 0);

        return purchaseTime.isAfter(start) && purchaseTime.isBefore(end);
    }
}
