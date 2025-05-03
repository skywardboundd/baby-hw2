package ru.hse.baby.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ToString(onlyExplicitlyIncluded = true)
public class Category {
    private long id;
    
    @ToString.Include
    private String name;
    
    private TransactionType type;


} 