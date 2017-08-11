package penfedcreditunion.studentcreditcardmockup.model;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SEAN on 7/28/17.
 */

public class Account {
    public String firstName;
    public String lastName;
    public String SSN;
    public String cardNum;
    public String email;
    public String password;
    public String accId;
    public double accCreditLimit;
    public double dailyLimit;
    public double gpa;
    public String schoolName;
    public double cashBackRate;
    public double creditUsed;
    public HashMap<String, Transaction> pendingTransactions;
    public HashMap<String, Transaction> completedTransactions;
    public double cashBackToDate;
    //public Map<String, Transaction> dailyTransactions;


    /**
     * default no-arg constructor
     */
    public Account() {

    }

    /**
     * actual constructor for initialization
     *
     * @param firstName first name of the user associated with account
     * @param lastName  last name of user associated w/ account
     * @param SSN       ssn of user on account
     * @param email     email of the account holder
     * @param password  password of the account holder
     * @param cardNum   card number of the account
     * @param accId     the account id associated with the account
     * @param gpa       the gpa for the account
     * @param schoolName    the school name for the student on the account
     * @param pendingTransactions a list of all pending transactions associated with this account
     * @param completedTransactions list of all completed transactions associated w/ account
     */
    public Account(String firstName, String lastName, String SSN,
                   String email, String password, String cardNum, String accId,
                   double gpa, String schoolName, HashMap<String, Transaction> pendingTransactions,
                   HashMap<String, Transaction> completedTransactions) {
                   //Map<String, Transaction> dailyTransactions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.SSN = SSN;
        this.cardNum = cardNum;
        this.email = email;
        this.password = password;
        this.accId = accId;
        dailyLimit = 50.00;
        accCreditLimit = 1000.00;
        creditUsed = getCreditUsed();
        this.gpa = gpa;
        cashBackRate = gpa / 2;
        this.schoolName = schoolName;
        this.pendingTransactions = pendingTransactions;
        this.completedTransactions = completedTransactions;
        this.cashBackToDate = 0.0;
        //this.dailyTransactions = dailyTransactions;
    }

    /**
     * convert account info to string
     * @return string rep of account
     */
    public String toString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return firstName + lastName + "\n" + cardNum + "\n" + email + "\n" + formatter.format(accCreditLimit);
    }

    public double getCreditUsed() {
        double totalCreditUsed = 0.0;
        if (pendingTransactions != null) {
            for (Transaction t : pendingTransactions.values()) {
                totalCreditUsed += t.transactionAmount;
            }
        }
        if (completedTransactions != null) {
            for (Transaction t : completedTransactions.values()) {
                totalCreditUsed += t.transactionAmount;
            }
        }
        return totalCreditUsed;
    }

    public void setPendingTransactions(HashMap<String, Transaction> map) {
        this.pendingTransactions = map;
    }

    public Map<String, Transaction> getPendingTransactions() {
        return this.pendingTransactions;
    }

    public void setCompletedTransactions(HashMap<String, Transaction> map) {
        this.completedTransactions = map;
    }

    public Map<String,Transaction> getCompletedTransactions() {
        return this.completedTransactions;
    }

    public double getCashBackToDate() {
        double totalCashBack = 0.0;
        if (completedTransactions != null) {
            for (Transaction t : completedTransactions.values()) {
                if (t.type == TransactionType.GAS || t.type == TransactionType.DINING) {
                    totalCashBack += (t.transactionAmount * ((cashBackRate + 1.00) / 100));
                } else {
                    totalCashBack += (t.transactionAmount * (cashBackRate / 100));
                }
            }
        }
        if (pendingTransactions != null) {
            for (Transaction t : pendingTransactions.values()) {
                if (t.type == TransactionType.GAS || t.type == TransactionType.DINING) {
                    totalCashBack += (t.transactionAmount * ((cashBackRate + 1.00) / 100));
                } else {
                    totalCashBack += (t.transactionAmount * (cashBackRate / 100));
                }
            }
        }
        return totalCashBack;
    }

    /*public void setDailyTransactions(Map<String, Transaction> map) {
        this.dailyTransactions = map;
    }*/

    /*public Map<String, Transaction> getDailyTransactions() {
        return this.dailyTransactions;
    }*/
}