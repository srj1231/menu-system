package com.saumya.lld.menu_system.entities;

import java.util.LinkedHashMap;
import java.util.Map;

public class Menu {

    public String restaurantId;
    public final Map<String, Category> categories;

    public Menu(String restaurantId) {
        this.restaurantId = restaurantId;
        this.categories = new LinkedHashMap<>();
    }

    public Menu copy() {
        Menu m = new Menu(restaurantId);
        categories.values().forEach(
                category -> m.categories.put(category.id, category.copy())
        );
        return m;
    }

    public void print() {
        System.out.println("=== Menu of Restaurant: " + restaurantId + " ===");
        for (Category c : categories.values()) {
            System.out.println("  [" + c.name + "] (" + c.id + ")");
            c.items.values().forEach(i -> System.out.println("     - " + i));
        }
    }
}
