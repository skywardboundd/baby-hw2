package ru.hse.baby.service;

import org.springframework.stereotype.Service;
import ru.hse.baby.model.Category;
import ru.hse.baby.model.TransactionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryService {
    private final List<Category> categories = new ArrayList<>();
    private long nextCategoryId = 1;

    public CategoryService() {

        addCategory("Зарплата", TransactionType.INCOME);
        addCategory("Подработка", TransactionType.INCOME);
        addCategory("Подарки", TransactionType.INCOME);
        addCategory("Инвестиции", TransactionType.INCOME);
        
        addCategory("Продукты", TransactionType.EXPENSE);
        addCategory("Транспорт", TransactionType.EXPENSE);
        addCategory("Развлечения", TransactionType.EXPENSE);
        addCategory("Жилье", TransactionType.EXPENSE);
        addCategory("Здоровье", TransactionType.EXPENSE);
    }


    public Category addCategory(String name, TransactionType type) {
        Category category = new Category(nextCategoryId++, name, type);
        categories.add(category);
        return category;
    }


    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public List<Category> getCategoriesByType(TransactionType type) {
        return categories.stream()
                .filter(category -> category.getType() == type)
                .collect(Collectors.toList());
    }

    public Category getCategoryById(long id) {
        return categories.stream()
                .filter(category -> category.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean deleteCategory(long id) {
        return categories.removeIf(category -> category.getId() == id);
    }

    public boolean updateCategory(long id, String name, TransactionType type) {
        for (Category category : categories) {
            if (category.getId() == id) {
                category.setName(name);
                category.setType(type);
                return true;
            }
        }
        return false;
    }
} 