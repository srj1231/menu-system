package com.saumya.lld.menu_system.store;

import com.saumya.lld.menu_system.entities.Category;
import com.saumya.lld.menu_system.entities.Item;
import com.saumya.lld.menu_system.entities.Menu;
import com.saumya.lld.menu_system.entities.Restaurant;

import java.util.Optional;

public interface MenuRepository {

    void saveRestaurant(Restaurant restaurant);

    boolean restaurantExists(String restaurantId);

    Menu getMenu(String restaurantId); // returns a copy of menu

    void saveCategory(Category category, String restaurantId);

    void saveItem(Item item, String restaurantId, String categoryId);

    void deleteItem(String restaurantId, String categoryId, String itemId);

    void deleteCategory(String restaurantId, String categoryId);

    Optional<Item> getItem(String restaurantId, String categoryId, String itemId);
}
