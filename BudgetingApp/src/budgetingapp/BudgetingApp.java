package budgetingapp;
import java.util.*;

// === User Class ===
class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public boolean authenticate(String pwd) { return this.password.equals(pwd); }
}

// === Authentication Manager ===
class Authentication {
    private static Authentication instance = null;
    private Map<String, User> users = new HashMap<>();

    private Authentication() {}

    public static Authentication getInstance() {
        if (instance == null) instance = new Authentication();
        return instance;
    }

    public boolean signup(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, password));
        return true;
    }

    public User login(String username, String password) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            return user.authenticate(password) ? user : null;
        }
        return null;
    }
}

// === Income Class ===
class Income {
    private String source;
    private double amount;

    public Income(String source, double amount) {
        this.source = source;
        this.amount = amount;
    }

    public double getAmount() { return amount; }
    public String toString() { return source + ": $" + amount; }
}

// === Expense Class ===
class Expense {
    private String category;
    private double amount;

    public Expense(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public double getAmount() { return amount; }
    public String toString() { return category + ": $" + amount; }
}

// === Goal Class ===
class Goal {
    private String name;
    private double target;

    public Goal(String name, double target) {
        this.name = name;
        this.target = target;
    }

    public String toString() { return name + " (Target: $" + target + ")"; }
}

// === Main Budgeting App ===
public class BudgetingApp {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Income> incomes = new ArrayList<>();
    private static List<Expense> expenses = new ArrayList<>();
    private static List<Goal> goals = new ArrayList<>();

    public static void main(String[] args) {
        Authentication auth = Authentication.getInstance();
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
        while (option != 0) {
            System.out.println("\n1. Add Income\n2. Add Expense\n3. Set Goal\n4. View Summary\n0. Exit");
            option = scanner.nextInt(); scanner.nextLine();

            switch (option) {
                case 1 -> addIncome();
                case 2 -> addExpense();
                case 3 -> addGoal();
                case 4 -> showSummary();
            }
        }
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

    private static void showSummary() {
        System.out.println("\n--- Incomes ---");
        incomes.forEach(System.out::println);
        System.out.println("Total Income: $" + incomes.stream().mapToDouble(Income::getAmount).sum());

        System.out.println("\n--- Expenses ---");
        expenses.forEach(System.out::println);
        System.out.println("Total Expenses: $" + expenses.stream().mapToDouble(Expense::getAmount).sum());

        System.out.println("\n--- Goals ---");
        goals.forEach(System.out::println);
    }
}
