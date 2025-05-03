package ru.hse.baby.service;

import org.springframework.stereotype.Service;
import ru.hse.baby.model.Category;
import ru.hse.baby.model.Transaction;
import ru.hse.baby.model.TransactionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TransactionService {
    private final List<Transaction> transactions = new ArrayList<>();
    private long nextTransactionId = 1;


    public Transaction addTransaction(String description, double amount, TransactionType type, Category category) {
        Transaction transaction = new Transaction(
                (int) nextTransactionId++,
                description,
                amount,
                type,
                LocalDateTime.now(),
                category
        );
        transactions.add(transaction);
        return transaction;
    }


    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }


    public List<Transaction> getTransactionsByType(TransactionType type) {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == type)
                .collect(Collectors.toList());
    }


    public List<Transaction> getTransactionsByCategory(Category category) {
        return transactions.stream()
                .filter(transaction -> transaction.getCategory().getId() == category.getId())
                .collect(Collectors.toList());
    }


    public boolean deleteTransaction(long id) {
        return transactions.removeIf(transaction -> transaction.getId() == id);
    }


    public double getTotalAmount(TransactionType type) {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == type)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


    public boolean updateTransaction(long id, String description, double amount, TransactionType type, Category category) {
        for (Transaction transaction : transactions) {
            if (transaction.getId() == id) {
                transaction.setDescription(description);
                transaction.setAmount(amount);
                transaction.setType(type);
                transaction.setCategory(category);
                return true;
            }
        }
        return false;
    }
} 