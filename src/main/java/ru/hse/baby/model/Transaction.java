package ru.hse.baby.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Transaction {
    private int id;
    private String description;
    private double amount;
    private TransactionType type;
    private LocalDateTime timestamp;
    private Category category;


} 