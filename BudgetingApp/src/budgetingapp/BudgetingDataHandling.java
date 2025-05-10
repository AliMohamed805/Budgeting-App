package budgetingapp;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Provides core data structures and utilities for the budgeting application.
 * <p>
 * Contains entity classes, authentication, data persistence, reporting, and AI prediction.
 * </p>
 */
public class BudgetingDataHandling {
    /**
     * Represents a user with a username and password.
     */
    static class User implements Serializable {
        /**
         * Constructs a new user.
         * @param username the username
         * @param password the password
         */
        private String username;
        private String password;
        /**
         * Constructs a new user.
         * @param username the username
         * @param password the password
         */
        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
        /**
         * Gets the username.
         * @return the username
         */
        public String getUsername() { return username; }
        /**
         * Checks if the provided password matches.
         * @param pwd the password to check
         * @return true if the password matches, false otherwise
         */
        public boolean authenticate(String pwd) { return this.password.equals(pwd); }
    }

    /**
     * Handles user authentication and registration.
     */
    static class Authentication {
        /**
         * Gets the singleton instance.
         * @return the authentication instance
         */
        private static Authentication instance = null;
        private Map<String, User> users = new HashMap<>();
        private Authentication() { users = UserStorage.loadUsers(); }
        public static Authentication getInstance() {
            if (instance == null) instance = new Authentication();
            return instance;
        }
        /**
         * Registers a new user.
         * @param username the username
         * @param password the password
         * @return true if successful, false if username exists
         */
        public boolean signup(String username, String password) {
            if (users.containsKey(username)) return false;
            users.put(username, new User(username, password));
            UserStorage.saveUsers(users);
            return true;
        }
        /**
         * Logs in a user.
         * @param username the username
         * @param password the password
         * @return the user if credentials are valid, null otherwise
         */
        public User login(String username, String password) {
            if (users.containsKey(username)) {
                User user = users.get(username);
                return user.authenticate(password) ? user : null;
            }
            return null;
        }
    }

    /**
     *  Represents an income entry.
     */
    static class Income implements Serializable {
        private String source;
        private double amount;
        /**
         * Constructs an income entry.
         * @param source the source of income
         * @param amount the amount of income
         */
        public Income(String source, double amount) {
            this.source = source;
            this.amount = amount;
        }
        /**
         * Gets the amount of income.
         * @return the income amount
         */
        public double getAmount() { return amount; }
        /**
         * Returns a string representation of the income.
         * @return the string representation
         */
        public String toString() { return source + ": $" + amount; }
    }

    /**
     * Represents an expense entry.
     */
    static class Expense implements Serializable {
        private String category;
        private double amount;
        /**
         * Constructs an expense entry.
         * @param category the expense category
         * @param amount the expense amount
         */
        public Expense(String category, double amount) {
            this.category = category;
            this.amount = amount;
        }
        /**
         * Gets the amount of the expense.
         * @return the expense amount
         */
        public double getAmount() { return amount; }
        /**
         * Returns a string representation of the expense.
         * @return the string representation
         */
        public String toString() { return category + ": $" + amount; }
    }

    /**
     * Represents a financial goal.
     */
    static class Goal implements Serializable {
        private String name;
        private double target;
        /**
         * Constructs a financial goal.
         * @param name the goal name
         * @param target the target amount
         */
        public Goal(String name, double target) {
            this.name = name;
            this.target = target;
        }
        /**
         * Returns a string representation of the goal.
         * @return the string representation
         */
        public String toString() { return name + " (Target: $" + target + ")"; }
    }

    /**
     * Represents a budget for a specific category.
     */
    static class Budget implements Serializable {
        private String category;
        private double limit;
        /**
         * Constructs a budget.
         * @param category the budget category
         * @param limit the budget limit
         */
        public Budget(String category, double limit) {
            this.category = category;
            this.limit = limit;
        }
        /**
         * Returns a string representation of the budget.
         * @return the string representation
         */
        public String toString() { return category + " Budget: $" + limit; }
    }

    /**
     * Represents a reminder with a message and date.
     */
    static class Reminder implements Serializable {
        private String message;
        private LocalDateTime dateTime;
        /**
         * Constructs a reminder.
         * @param message the reminder message
         * @param dateTime the date and time of the reminder
         */
        public Reminder(String message, LocalDateTime dateTime) {
            this.message = message;
            this.dateTime = dateTime;
        }
        /**
         * Returns a string representation of the reminder.
         * @return the string representation
         */
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm");
            return "Reminder: " + message + " on " + dateTime.format(formatter);
        }
    }

    /**
     * Represents a debt to a creditor.
     */
    static class Debt implements Serializable {
        private String creditor;
        private double amount;
        /**
         * Constructs a debt entry.
         * @param creditor the creditor's name
         * @param amount the amount owed
         */
        public Debt(String creditor, double amount) {
            this.creditor = creditor;
            this.amount = amount;
        }
        /**
         * Gets the amount of the debt.
         * @return the debt amount
         */
        public double getAmount() {
            return amount;
        }
        /**
         * Returns a string representation of the debt.
         * @return the string representation
         */
        public String toString() { return "Debt to " + creditor + ": $" + amount; }
    }

    /**
     * Represents a donation to an organization.
     */
    static class Donation implements Serializable {
        private String organization;
        private double amount;
        /**
         * Constructs a donation entry.
         * @param organization the organization name
         * @param amount the donation amount
         */
        public Donation(String organization, double amount) {
            this.organization = organization;
            this.amount = amount;
        }
        /**
         * Returns a string representation of the donation.
         * @return the string representation
         */
        public String toString() { return "Donation to " + organization + ": $" + amount; }
    }

    /**
     * Utility class for saving and loading lists of data using serialization.
     */
    static class DataStorage {
        /**
         * Saves a list to a file.
         * @param list the list to save
         * @param filename the file name
         */
        public static void saveList(List<?> list, String filename) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
                out.writeObject(new ArrayList<>(list));
            } catch (IOException e) {
                System.out.println("Error saving data: " + e.getMessage());
            }
        }
        @SuppressWarnings("unchecked")
        /**
         * Loads a list from a file.
         * @param filename the file name
         * @param <T> the type of list elements
         * @return the loaded list, or empty if not found
         */
        public static <T> List<T> loadList(String filename) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
                return (List<T>) in.readObject();
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
    }

    /**
     * Strategy interface for generating reports.
     */
    public interface ReportStrategy {
        /**
         * Generates a report file from the provided data.
         * @param incomes list of incomes
         * @param expenses list of expenses
         * @param budgets list of budgets
         * @param goals list of goals
         * @param debts list of debts
         * @param reminders list of reminders
         * @param donations list of donations
         * @param filename the output file name
         */
        void generate(
                List<?> incomes,
                List<?> expenses,
                List<?> budgets,
                List<?> goals,
                List<?> debts,
                List<?> reminders,
                List<?> donations,
                String filename
        );
    }

    /**
     * Generates a CSV report containing all budgeting entities.
     */
    public static class CSVReportStrategy implements ReportStrategy {
        public void generate(
                List<?> incomes,
                List<?> expenses,
                List<?> budgets,
                List<?> goals,
                List<?> debts,
                List<?> reminders,
                List<?> donations,
                String filename
        ) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println("Type,Description,Amount");
                for (Object i : incomes) writer.println("Income," + i.toString());
                for (Object e : expenses) writer.println("Expense," + e.toString());
                for (Object b : budgets) writer.println("Budget," + b.toString());
                for (Object g : goals) writer.println("Goal," + g.toString());
                for (Object d : debts) writer.println("Debt," + d.toString());
                for (Object r : reminders) writer.println("Reminder," + r.toString());
                for (Object don : donations) writer.println("Donation," + don.toString());
            } catch (IOException e) {
                System.out.println("Error writing CSV: " + e.getMessage());
            }
        }
    }

    /**
     * Provides AI-based spending prediction (mock implementation).
     */
    static class AIPrediction {
        /**
         * Predicts spending trends based on expenses.
         * @param expenses the list of expenses
         * @return a prediction string
         */
        public static String predictSpendingTrend(List<?> expenses) {
            return "Prediction: Your spending is stable.";
        }
    }
    

    public static class Wallet {
        private int walletID;
        private double balance;
        private List<Transaction> transactions;
 
        public Wallet(int walletID, double balance) {
            this.walletID = walletID;
            this.balance = balance;
        }
        public Wallet() {
            this.walletID = 0;
            this.balance = 0.0;
            this.transactions = new ArrayList<>();
        }
        public void linkWallet() {
            System.out.println("Wallet linked successfully.");
        }
        public void veiwBalance() {
            System.out.println("Your current balance is: " + balance);
        }
        public int getWalletID() {
            return walletID;
        }
        public double getBalance() {
            return balance;
        }

        public void addTransaction(Transaction transaction) {
            transactions.add(transaction);
            if(transaction.getCategory().equals("Expense")) {
                balance -= transaction.getAmount();
            } else if(transaction.getCategory().equals("Income")) {
                balance += transaction.getAmount();
            } 
            System.out.println("Transaction added successfully.");
    }
    public static class Transaction {
        private String transactionID;
        private double amount;
        private String category; 
        private LocalDateTime dateTime;
        private boolean reccurring;

        public Transaction(String transactionID, double amount, String type) {
            this.transactionID = transactionID;
            this.amount = amount;
            this.category = type;
            this.dateTime = LocalDateTime.now();
            this.reccurring = true;
        }

        public void cancelTransaction(String transactionID) {
            reccurring = false;
            System.out.println("Transaction " + transactionID + " has been cancelled.");
        }
        public void viewTransaction() {
            System.out.println("Transaction ID: " + transactionID);
            System.out.println("Amount: " + amount);
            System.out.println("Category: " + category);
            System.out.println("Date and Time: " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm")));
        }
        public void scheduleTransaction(String transactionID, LocalDateTime dateTime) {
            this.dateTime = dateTime;
            reccurring = true;
            System.out.println("Transaction " + transactionID + " has been scheduled for " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm")));
        }


        public String getTransactionID() {
            return transactionID;
        }
        public double getAmount() {
            return amount;
        }
        public String getCategory() {
            return category;
        }
    }
    
}