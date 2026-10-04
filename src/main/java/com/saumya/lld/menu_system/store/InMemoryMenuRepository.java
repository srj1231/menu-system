package com.saumya.lld.menu_system.store;

import com.saumya.lld.menu_system.entities.Category;
import com.saumya.lld.menu_system.entities.Item;
import com.saumya.lld.menu_system.entities.Menu;
import com.saumya.lld.menu_system.entities.Restaurant;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class InMemoryMenuRepository implements MenuRepository {

    private final Map<String, Restaurant> restaurants = new HashMap<>();
    private final Map<String, Menu> menus = new HashMap<>();

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurants.put(restaurant.getId(), restaurant);
        menus.putIfAbsent(restaurant.getId(), new Menu(restaurant.getId())); // create new menu if not exists
    }

    @Override
    public boolean restaurantExists(String restaurantId) {
        return restaurants.containsKey(restaurantId);
    }

    @Override
    public Menu getMenu(String restaurantId) {
        Menu m = menus.get(restaurantId);
        if (m == null) {
            throw new NoSuchElementException("No such restaurant: " + restaurantId);
        }
        return m.copy();
    }

    @Override
    public void saveCategory(Category category, String restaurantId) {
        Menu m = rawMenu(restaurantId);
        Category existing = m.categories.get(category.id);

        if (existing != null) existing.name = category.name;     // update keeps items
        else m.categories.put(category.id, category);
    }

    private Menu rawMenu(String restaurantId) {
        Menu m = menus.get(restaurantId);
        if (m == null) {
            throw new NoSuchElementException("No such restaurant: " + restaurantId);
        }
        return m;
    }

    @Override
    public void saveItem(Item item, String restaurantId, String categoryId) {
        Category c = rawCategory(restaurantId, categoryId);
        c.items.put(item.id, item);
    }

    private Category rawCategory(String restaurantId, String categoryId) {
        Category c = rawMenu(restaurantId).categories.get(categoryId);
        if (c == null) {
            throw new NoSuchElementException("Category not found: " + categoryId);
        }

        return c;
    }

    @Override
    public void deleteItem(String restaurantId, String categoryId, String itemId) {
        Category c = rawCategory(restaurantId, categoryId);
        c.items.remove(itemId);
    }

    @Override
    public void deleteCategory(String restaurantId, String categoryId) {
        Menu m = rawMenu(restaurantId);
        m.categories.remove(categoryId);
    }

    @Override
    public Optional<Item> getItem(String restaurantId, String categoryId, String itemId) {
        Item item = rawCategory(restaurantId, categoryId).items.get(itemId);
        return Optional.ofNullable(item);
    }
}
