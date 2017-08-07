package penfedcreditunion.studentcreditcardmockup.controllers;


import java.util.Scanner;

import penfedcreditunion.studentcreditcardmockup.model.Account;

import static penfedcreditunion.studentcreditcardmockup.controllers.SubmissionHandlerActivity.findUserEmail;


/**
 * Created by SEAN on 8/2/17.
 */

public class SubmitTransactionActivity {

    private static Account tempAccount;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.println("Welcome to the Transaction submission service.");
        System.out.println("What would you like to do today?");
        System.out.println("Modify an existing transaction on a user's account, " +
                "or add a new transaction to a user's account?");
        System.out.println("Please type 'm' or 'modify' (not case-sensitive) to modify an existing " +
                "transaction, or 'a' or 'add' (not case-sensitive) to add a new transaction.");
        String selection = scanner.nextLine();
        while (!validEntry(selection)) {
            System.out.println("I'm sorry, you've entered an invalid option. Please choose again.");
            selection = scanner.nextLine();
        }

        System.out.println("You chose to " + determineSelection(selection) + " a user's account.");
        System.out.println("Is this the correct choice? ('y' or 'yes' for yes and 'n' or 'no' for no)");
        String validation = scanner.nextLine();

        if (validation.equals("n") || validation.equals("no")) {
            while (!validation.equals("y") && !validation.equals("yes")) {
                System.out.println("Would you like to modify a transaction or add a new transaction?");
                selection = scanner.nextLine();
                System.out.println("You chose to " + determineSelection(selection) + " an account.");
                System.out.println("Is this the correct choice? ('y' or 'yes' for yes and 'n' or 'no' for no)");
                validation = scanner.nextLine();
            }
        }

        System.out.println("What is the email of the account you're trying to "
                + determineSelection(selection) + "?");
        String userEmail = scanner.nextLine();

        //tempAccount = findUserEmail(userEmail);

        while (tempAccount == null) {
            System.out.println("No account exists with that email address.");
            System.out.println("Would you like to look for a different email?");
            String retry = scanner.nextLine();
            if (retry.equals("n") || retry.equals("no")) {
                System.out.println("Thank you for using the Transaction" +
                        " Submission service. Have a great day.");
            } else if (retry.equals("y") || retry.equals("yes")) {
                System.out.println("What is the email of the account you're trying to "
                        + determineSelection(selection) + "?");
                userEmail = scanner.nextLine();

                findUserEmail(userEmail);
            } else {
                while (!retry.equals("n") && !retry.equals("no") && !retry.equals("y") && !retry.equals("yes")) {
                    System.out.println("I'm sorry you've entered an invalid response." +
                            " Please try again.");
                    retry = scanner.nextLine();
                }
            }
        }

        System.out.println("Found user " + userEmail + "!");

    }

    public static String determineSelection(String selection) {
        if (selection.equals("m") || selection.equals("modify")) {
            return "modify a transaction on";
        } else {
            return "add a transaction to";
        }
    }

    public static boolean validEntry(String selection) {
        if (selection.equals("m")) {
            return true;
        } else if (selection.equals("modify")) {
            return true;
        } else if (selection.equals("a")) {
            return true;
        } else if (selection.equals("add")) {
            return true;
        } else {
            return false;
        }
    }
}
