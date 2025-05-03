package ru.hse.baby;

import ru.hse.baby.model.Category;
import ru.hse.baby.model.Transaction;
import ru.hse.baby.model.TransactionType;
import ru.hse.baby.service.CategoryService;
import ru.hse.baby.service.TransactionService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class BabyFinanceApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    
    private static final CategoryService categoryService = new CategoryService();
    private static final TransactionService transactionService = new TransactionService();

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в Baby Finance!");
        boolean running = true;
        
        while (running) {
            printMainMenu();
            int choice = getIntInput("Выберите опцию: ");
            
            switch (choice) {
                case 1:
                    addTransaction();
                    break;
                case 2:
                    viewTransactions();
                    break;
                case 3:
                    addCategory();
                    break;
                case 4:
                    viewCategories();
                    break;
                case 5:
                    showStatistics();
                    break;
                case 0:
                    running = false;
                    System.out.println("Спасибо за использование Baby Finance! До свидания!");
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, выберите опцию из меню.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Добавить транзакцию");
        System.out.println("2. Просмотреть транзакции");
        System.out.println("3. Добавить категорию");
        System.out.println("4. Просмотреть категории");
        System.out.println("5. Показать статистику");
        System.out.println("0. Выход");
    }

    private static void addTransaction() {
        System.out.println("\n=== ДОБАВЛЕНИЕ ТРАНЗАКЦИИ ===");
        
        System.out.println("Выберите тип транзакции:");
        System.out.println("1. " + TransactionType.INCOME.getDisplayName());
        System.out.println("2. " + TransactionType.EXPENSE.getDisplayName());
        int typeChoice = getIntInput("Введите номер: ");
        TransactionType type = (typeChoice == 1) ? TransactionType.INCOME : TransactionType.EXPENSE;
        
        List<Category> availableCategories = categoryService.getCategoriesByType(type);
        if (availableCategories.isEmpty()) {
            System.out.println("Нет доступных категорий для этого типа транзакций. Пожалуйста, сначала добавьте категорию.");
            return;
        }
        
        System.out.println("Доступные категории:");
        for (int i = 0; i < availableCategories.size(); i++) {
            System.out.println((i + 1) + ". " + availableCategories.get(i).getName());
        }
        
        int categoryIndex = getIntInput("Выберите категорию (номер): ") - 1;
        if (categoryIndex < 0 || categoryIndex >= availableCategories.size()) {
            System.out.println("Неверный выбор категории.");
            return;
        }
        
        Category selectedCategory = availableCategories.get(categoryIndex);
        
        System.out.print("Введите описание: ");
        String description = scanner.nextLine();
        
        double amount = getDoubleInput("Введите сумму: ");
        
        Transaction transaction = transactionService.addTransaction(description, amount, type, selectedCategory);
        
        System.out.println("Транзакция успешно добавлена!");
        System.out.println("ID: " + transaction.getId());
        System.out.println("Тип: " + transaction.getType().getDisplayName());
        System.out.println("Категория: " + transaction.getCategory().getName());
        System.out.println("Описание: " + transaction.getDescription());
        System.out.println("Сумма: " + transaction.getAmount());
        System.out.println("Дата: " + transaction.getTimestamp().format(dateFormatter));
    }

    private static void viewTransactions() {
        System.out.println("\n=== ПРОСМОТР ТРАНЗАКЦИЙ ===");
        System.out.println("1. Все транзакции");
        System.out.println("2. Доходы");
        System.out.println("3. Расходы");
        System.out.println("0. Назад");
        
        int choice = getIntInput("Выберите опцию: ");
        List<Transaction> transactions;
        
        switch (choice) {
            case 1:
                transactions = transactionService.getAllTransactions();
                printTransactions("Все транзакции", transactions);
                break;
            case 2:
                transactions = transactionService.getTransactionsByType(TransactionType.INCOME);
                printTransactions("Доходы", transactions);
                break;
            case 3:
                transactions = transactionService.getTransactionsByType(TransactionType.EXPENSE);
                printTransactions("Расходы", transactions);
                break;
            case 0:
                return;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void printTransactions(String title, List<Transaction> transactions) {
        System.out.println("\n=== " + title.toUpperCase() + " ===");
        
        if (transactions.isEmpty()) {
            System.out.println("Транзакции не найдены.");
            return;
        }
        
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-4s | %-12s | %-15s | %-20s | %-10s%n", 
                "ID", "Дата", "Категория", "Описание", "Сумма");
        System.out.println("-------------------------------------------------------------");
        
        for (Transaction transaction : transactions) {
            System.out.printf("%-4d | %-12s | %-15s | %-20s | %10.2f%n",
                    transaction.getId(),
                    transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    transaction.getCategory().getName(),
                    transaction.getDescription().length() > 20 
                            ? transaction.getDescription().substring(0, 17) + "..." 
                            : transaction.getDescription(),
                    transaction.getAmount());
        }
        
        System.out.println("-------------------------------------------------------------");
    }

    private static void addCategory() {
        System.out.println("\n=== ДОБАВЛЕНИЕ КАТЕГОРИИ ===");
        
        System.out.println("Выберите тип категории:");
        System.out.println("1. " + TransactionType.INCOME.getDisplayName());
        System.out.println("2. " + TransactionType.EXPENSE.getDisplayName());
        int typeChoice = getIntInput("Введите номер: ");
        TransactionType type = (typeChoice == 1) ? TransactionType.INCOME : TransactionType.EXPENSE;
        
        System.out.print("Введите название категории: ");
        String name = scanner.nextLine();
        
        if (name.trim().isEmpty()) {
            System.out.println("Название категории не может быть пустым.");
            return;
        }
        
        Category category = categoryService.addCategory(name, type);
        
        System.out.println("Категория успешно добавлена!");
        System.out.println("ID: " + category.getId());
        System.out.println("Название: " + category.getName());
        System.out.println("Тип: " + category.getType().getDisplayName());
    }

    private static void viewCategories() {
        System.out.println("\n=== ПРОСМОТР КАТЕГОРИЙ ===");
        System.out.println("1. Все категории");
        System.out.println("2. Категории доходов");
        System.out.println("3. Категории расходов");
        System.out.println("0. Назад");
        
        int choice = getIntInput("Выберите опцию: ");
        List<Category> categories;
        
        switch (choice) {
            case 1:
                categories = categoryService.getAllCategories();
                printCategories("Все категории", categories);
                break;
            case 2:
                categories = categoryService.getCategoriesByType(TransactionType.INCOME);
                printCategories("Категории доходов", categories);
                break;
            case 3:
                categories = categoryService.getCategoriesByType(TransactionType.EXPENSE);
                printCategories("Категории расходов", categories);
                break;
            case 0:
                return;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void printCategories(String title, List<Category> categories) {
        System.out.println("\n=== " + title.toUpperCase() + " ===");
        
        if (categories.isEmpty()) {
            System.out.println("Категории не найдены.");
            return;
        }
        
        System.out.println("-----------------------------");
        System.out.printf("%-4s | %-20s | %-10s%n", "ID", "Название", "Тип");
        System.out.println("-----------------------------");
        
        for (Category category : categories) {
            System.out.printf("%-4d | %-20s | %-10s%n",
                    category.getId(),
                    category.getName(),
                    category.getType().getDisplayName());
        }
        
        System.out.println("-----------------------------");
    }

    private static void showStatistics() {
        System.out.println("\n=== СТАТИСТИКА ===");
        
        double totalIncome = transactionService.getTotalAmount(TransactionType.INCOME);
        double totalExpense = transactionService.getTotalAmount(TransactionType.EXPENSE);
        double balance = totalIncome - totalExpense;
        
        System.out.println("Общий доход: " + totalIncome);
        System.out.println("Общий расход: " + totalExpense);
        System.out.println("Баланс: " + balance);
        
        System.out.println("\n=== Статистика по категориям доходов ===");
        printCategoryStatistics(TransactionType.INCOME);
        
        System.out.println("\n=== Статистика по категориям расходов ===");
        printCategoryStatistics(TransactionType.EXPENSE);
    }

    private static void printCategoryStatistics(TransactionType type) {
        List<Category> categories = categoryService.getCategoriesByType(type);
        
        if (categories.isEmpty()) {
            System.out.println("Категории не найдены.");
            return;
        }
        
        double total = transactionService.getTotalAmount(type);
        
        for (Category category : categories) {
            List<Transaction> transactions = transactionService.getTransactionsByCategory(category);
            
            if (!transactions.isEmpty()) {
                double categoryTotal = transactions.stream()
                        .mapToDouble(Transaction::getAmount)
                        .sum();
                
                double percentage = (total > 0) ? (categoryTotal / total) * 100 : 0;
                
                System.out.printf("%-20s: %10.2f (%5.2f%%)%n", 
                        category.getName(), categoryTotal, percentage);
            }
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите целое число.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите корректное число.");
            }
        }
    }
} 