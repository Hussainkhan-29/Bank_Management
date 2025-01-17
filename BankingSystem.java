import java.util.ArrayList;
import java.util.Scanner;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

class Customer extends User {
    private double balance;
    private boolean isSaving;
    private ArrayList<Transaction> transactions;

    public Customer(String username, String password, boolean isSaving) {
        super(username, password);
        this.balance = 0.0;
        this.isSaving = isSaving;
        this.transactions = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            double depositAmount = amount;
            if (isSaving) {
                depositAmount += amount * BankingSystem.DEPOSIT_INTEREST;
            }
            balance += depositAmount;
            transactions.add(new Transaction("Deposit", amount, balance));
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            double tax = amount * BankingSystem.WITHDRAW_TAX;
            balance -= (amount + tax);
            transactions.add(new Transaction("Withdraw", amount, balance));
        }
    }

    public void displayDetails() {
        System.out.println("Username: " + getUsername());
        System.out.println("Balance: " + balance);
        System.out.println("Account Type: " + (isSaving ? "Savings" : "Current"));
    }

    public void showTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transaction history.");
            return;
        }
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}

class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    public void displayDetails() {
        System.out.println("Admin Username: " + getUsername());
    }
}

class Transaction {
    private String type;
    private double amount;
    private double balanceAfter;

    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    @Override
    public String toString() {
        return "Transaction: " + type + ", Amount: " + amount + ", Balance After: " + balanceAfter;
    }
}

public class BankingSystem {
    public static final double WITHDRAW_TAX = 0.02;
    public static final double DEPOSIT_INTEREST = 0.03;

    private static final ArrayList<Customer> customers = new ArrayList<>();
    private static final Admin admin = new Admin("admin", "admin123");
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Customer Login\n2. Admin Login\n3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    loginCustomer();
                    break;
                case 2:
                    adminMenu();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void adminMenu() {
        scanner.nextLine();
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if (admin.authenticate(username, password)) {
            System.out.println("Admin login successful!");
            while (true) {
                System.out.println("\n1. Create Account\n2. View All Customers\n3. Logout");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        viewAllCustomers();
                        break;
                    case 3:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void createAccount() {
        scanner.nextLine();
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        System.out.print("Select account type (1 for Savings, 2 for Current): ");
        int accountType = scanner.nextInt();

        boolean isSaving = (accountType == 1);
        customers.add(new Customer(username, password, isSaving));
        System.out.println("Account created successfully for " + username);
    }

    private static void viewAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers available.");
            return;
        }

        for (Customer customer : customers) {
            customer.displayDetails();
        }
    }

    private static void loginCustomer() {
        scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (Customer customer : customers) {
            if (customer.authenticate(username, password)) {
                System.out.println("Login successful!");
                customerMenu(customer);
                return;
            }
        }
        System.out.println("Invalid username or password.");
    }

    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n1. Deposit\n2. Withdraw\n3. Check Balance\n4. View Transaction History\n5. Logout");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    customer.deposit(depositAmount);
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    customer.withdraw(withdrawAmount);
                    break;
                case 3:
                    System.out.println("Current balance: " + customer.getBalance());
                    break;
                case 4:
                    customer.showTransactions();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
