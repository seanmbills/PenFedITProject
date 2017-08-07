package penfedcreditunion.studentcreditcardmockup.model;

import android.icu.text.NumberFormat;

/**
 * Created by SEAN on 7/30/17.
 */

public class Transaction {
    public double transactionAmount;
    public String transactionDate;
    public String businessEndpoint;
    public String status;

    /**
     * default no-arg constructor
     */
    public Transaction() {

    }

    /**
     * actual constructor for each and every transaction in the application
     * @param transactionAmount the amount of the transaction
     * @param transactionDate the transaction date
     * @param businessEndpoint the location at which the transaction was made
     *                         (what store essentially)
     * @param status the status of the transaction ("PENDING", "COMPLETE")
     */
    public Transaction(double transactionAmount, String transactionDate, String businessEndpoint,
                       String status) {
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.businessEndpoint = businessEndpoint;
        this.status = status;
    }

    /**
     * convert information for transaction to a String representation
     * @return the string for the transaction
     */
    public String toString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return businessEndpoint
                + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
                + formatter.format(transactionAmount) + "\n" + transactionDate
                + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + status;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getStatus() {
        return status;
    }

    public String getBusinessEndpoint() {
        return businessEndpoint;
    }
}
