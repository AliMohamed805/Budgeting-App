package budgetingapp;
import budgetingapp.BudgetingDataHandling.AIPrediction;
import budgetingapp.BudgetingDataHandling.Authentication;
import budgetingapp.BudgetingDataHandling.Budget;
import budgetingapp.BudgetingDataHandling.CSVReportStrategy;
import budgetingapp.BudgetingDataHandling.DataStorage;
import budgetingapp.BudgetingDataHandling.Debt;
import budgetingapp.BudgetingDataHandling.Donation;
import budgetingapp.BudgetingDataHandling.Expense;
import budgetingapp.BudgetingDataHandling.Goal;
import budgetingapp.BudgetingDataHandling.Income;
import budgetingapp.BudgetingDataHandling.Reminder;
import budgetingapp.BudgetingDataHandling.ReportStrategy;
import budgetingapp.BudgetingDataHandling.User;
import budgetingapp.BudgetingDataHandling.Wallet;
import budgetingapp.BudgetingDataHandling.Transaction;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Main entry point for the budgeting application.
 * <p>
 * Handles user authentication, main menu navigation, and user interactions
 * for managing incomes, expenses, goals, budgets, reminders, debts, and donations.
 * Also supports report generation and AI-based spending prediction.
 * </p>
 */

// === Main Budgeting App ===
public class BudgetingApp {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Income> incomes = DataStorage.loadList("incomes.dat");
    private static List<Expense> expenses = DataStorage.loadList("expenses.dat");
    private static List<Goal> goals = DataStorage.loadList("goals.dat");
    private static List<Budget> budgets = DataStorage.loadList("budgets.dat");
    private static List<Debt> debts = DataStorage.loadList("debts.dat");
    private static List<Reminder> reminders = DataStorage.loadList("reminders.dat");
    private static List<Donation> donations = DataStorage.loadList("donations.dat");
    private static List<Wallet> wallets = DataStorage.loadList("wallets.dat");
    private static List<Transaction> transactions = DataStorage.loadList("transactions.dat");

    /**
     * Application main method. Handles user authentication and menu navigation.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Authentication auth = Authentication.getInstance();
        Wallet wallet = new Wallet();
        User currentUser = null;

        while (currentUser == null) {
            System.out.println("1. Sign Up\n2. Log In\nChoose option: ");
            int choice = scanner.nextInt(); scanner.nextLine();
            System.out.print("Username: "); String username = scanner.nextLine();
            System.out.print("Password: "); String password = scanner.nextLine();

            if (choice == 1) {
                if (auth.signup(username, password)) {
                    System.out.println("Signup successful.");
                } else {
                    System.out.println("Username already exists.");
                }
            } else if (choice == 2) {
                currentUser = auth.login(username, password);
                if (currentUser == null) System.out.println("Invalid credentials.");
            }
        }

        // Main menu
        int option = -1;
        System.out.println("Welcome, " + currentUser.getUsername() + "!");
        while (option != 0) {
            System.out.println("\n1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. Set Goal");
            System.out.println("4. Add Budget");
            System.out.println("5. Add Reminder");
            System.out.println("6. Add Debt");
            System.out.println("7. Add Donation");
            System.out.println("8. View Summary");
            System.out.println("9. Generate CSV Report");
            System.out.println("10. AI Prediction");
            System.out.println("11. Add Wallet");
            System.out.println("12. Add Transaction");
            System.out.println("0. Exit");
            option = scanner.nextInt(); scanner.nextLine();

            switch (option) {
                case 1 -> addIncome();
                case 2 -> addExpense();
                case 3 -> addGoal();
                case 4 -> addBudget();
                case 5 -> addReminder();
                case 6 -> addDebt();
                case 7 -> addDonation();
                case 8 -> showSummary();
                case 9 -> generateReport();
                case 10 -> showAIPrediction();
                case 11 -> addWallet();
                case 12 -> addTransaction();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid option. Try again.");
            }
        }
        saveAll();
    }

    private static void addIncome() {
        System.out.print("Income Source: "); String source = scanner.nextLine();
        System.out.print("Amount: "); double amount = scanner.nextDouble(); scanner.nextLine();
        incomes.add(new Income(source, amount));
    }

    private static void addExpense() {
        System.out.print("Expense Category: "); String category = scanner.nextLine();
        System.out.print("Amount: "); double amount = scanner.nextDouble(); scanner.nextLine();
        expenses.add(new Expense(category, amount));
    }

    private static void addGoal() {
        System.out.print("Goal Name: "); String name = scanner.nextLine();
        System.out.print("Target Amount: "); double target = scanner.nextDouble(); scanner.nextLine();
        goals.add(new Goal(name, target));
    }

    private static void addBudget() {
        System.out.print("Budget Category: "); String category = scanner.nextLine();
        System.out.print("Limit: "); double limit = scanner.nextDouble(); scanner.nextLine();
        budgets.add(new Budget(category, limit));
    }

    private static void addReminder() {
        System.out.print("Message: ");
        String message = scanner.nextLine();
        System.out.print("Date and Time (yyyy-MM-dd HH:mm): ");
        String dateTimeStr = scanner.nextLine();
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            reminders.add(new Reminder(message, dateTime));
        } catch (Exception e) {
            System.out.println("Invalid date/time format.");
        }
    }

    private static void addDebt() {
        System.out.print("Creditor: "); String creditor = scanner.nextLine();
        System.out.print("Amount: "); double amount = scanner.nextDouble(); scanner.nextLine();
        debts.add(new Debt(creditor, amount));
    }

    private static void addDonation() {
        System.out.print("Organization: "); String org = scanner.nextLine();
        System.out.print("Amount: "); double amount = scanner.nextDouble(); scanner.nextLine();
        donations.add(new Donation(org, amount));
    }
    
    private static void addWallet() {
        System.out.print("WalletID: "); int ID = scanner.nextInt();
        System.out.print("Amount: "); double amount = scanner.nextDouble(); scanner.nextLine();
        wallets.add(new Wallet(ID, amount));
    }
    
    private static void addTransaction() {
        System.out.print("TransactionID: "); int ID = scanner.nextInt();
        System.out.print("Amount: "); double amount = scanner.nextDouble();
        System.out.print("Transaction Type (Income/Expense): "); String type = scanner.nextLine(); scanner.nextLine();
        transactions.add(new Transaction(ID, amount, type));
    }

    private static void showSummary() {
        System.out.println("\n--- Incomes ---");
        incomes.forEach(System.out::println);
        System.out.println("Total Income: $" + incomes.stream().mapToDouble(Income::getAmount).sum());

        System.out.println("\n--- Expenses ---");
        expenses.forEach(System.out::println);
        System.out.println("Total Expenses: $" + expenses.stream().mapToDouble(Expense::getAmount).sum());

        System.out.println("\n--- Budgets ---");
        budgets.forEach(System.out::println);

        System.out.println("\n--- Goals ---");
        goals.forEach(System.out::println);

        System.out.println("\n--- Debts ---");
        debts.forEach(System.out::println);
        System.out.println("Total Debts: $" + debts.stream().mapToDouble(Debt::getAmount).sum());

        System.out.println("\n--- Reminders ---");
        reminders.forEach(System.out::println);

        System.out.println("\n--- Donations ---");
        donations.forEach(System.out::println);
        
        System.out.println("\n--- Wallets ---"); 
        wallets.forEach(System.out::println);
        
        System.out.println("\n--- transactions ---"); 
        transactions.forEach(System.out::println);

    }

    private static void generateReport() {
        ReportStrategy report = new CSVReportStrategy();
        report.generate(
                incomes, expenses, budgets, goals, debts, reminders, donations, wallets, transactions, "report.csv"
        );
        System.out.println("CSV report generated as report.csv");
    }

    private static void showAIPrediction() {
        String prediction = AIPrediction.predictSpendingTrend(expenses);
        System.out.println(prediction);
    }

    /**
     * Saves all financial data to persistent storage.
     */
    private static void saveAll() {
        DataStorage.saveList(incomes, "incomes.dat");
        DataStorage.saveList(expenses, "expenses.dat");
        DataStorage.saveList(goals, "goals.dat");
        DataStorage.saveList(budgets, "budgets.dat");
        DataStorage.saveList(debts, "debts.dat");
        DataStorage.saveList(reminders, "reminders.dat");
        DataStorage.saveList(donations, "donations.dat");
        DataStorage.saveList(wallets, "wallets.dat");
        DataStorage.saveList(transactions, "transactions.dat");
    }

}
