package ru.hse.baby.model;

import lombok.Getter;


public enum TransactionType {
    INCOME("Доход"),
    EXPENSE("Расход");

    @Getter
    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }


} 