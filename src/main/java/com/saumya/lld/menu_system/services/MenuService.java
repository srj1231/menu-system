package com.saumya.lld.menu_system.services;

import com.saumya.lld.menu_system.entities.Category;
import com.saumya.lld.menu_system.entities.Item;
import com.saumya.lld.menu_system.entities.Menu;
import com.saumya.lld.menu_system.enums.ItemStatus;
import com.saumya.lld.menu_system.store.MenuCache;
import com.saumya.lld.menu_system.store.MenuRepository;
import com.saumya.lld.menu_system.store.RestaurantRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuCache menuCache;
    private final RestaurantRepository restaurants;
    private final AtomicInteger seq = new AtomicInteger(1);

    // simple counters to show cache behaviour
    public int cacheHits = 0, cacheMisses = 0;

    public MenuService(MenuRepository menuRepository, MenuCache menuCache, RestaurantRepository restaurants) {
        this.menuRepository = menuRepository;
        this.menuCache = menuCache;
        this.restaurants = restaurants;
    }

    private boolean restaurantExists(String restaurantId) {
        return restaurants.exists(restaurantId);
    }

    // READ (cache-aside)
    public Menu getMenu(String restaurantId) {
        if(!restaurantExists(restaurantId)) {
            throw new NoSuchElementException("Restaurant not found: " + restaurantId);
        }

        Optional<Menu> cachedMenu = menuCache.getMenu(restaurantId);
        if(cachedMenu.isPresent()) {
            cacheHits++;
            return cachedMenu.get();
        }

        cacheMisses++;
        Menu menu = menuRepository.getMenu(restaurantId);
        menuCache.putMenu(restaurantId, menu);
        return menu;
    }

    // WRITE (update db, then invalidate cache)
    public String createCategory(String restaurantId, String name) {
        if(!restaurantExists(restaurantId)) {
            throw new NoSuchElementException("Restaurant not found: " + restaurantId);
        }

        Category newCategory = new Category(restaurantId, name);
        menuRepository.saveCategory(newCategory, restaurantId);
        menuCache.invalidate(restaurantId);
        return newCategory.getId();
    }

    public void updateCategory(String restaurantId, String categoryId, String newName) {
        if(!restaurantExists(restaurantId)) {
            throw new NoSuchElementException("Restaurant not found: " + restaurantId);
        }

        menuRepository.saveCategory(new Category(categoryId, newName), restaurantId);
        menuCache.invalidate(restaurantId);
    }

    public void deleteCategory(String restaurantId, String categoryId) {
        if(!restaurantExists(restaurantId)) {
            throw new NoSuchElementException("Restaurant not found: " + restaurantId);
        }

        menuRepository.deleteCategory(restaurantId, categoryId);
        menuCache.invalidate(restaurantId);
    }

    public String createItem(String restaurantId, String categoryId, String itemName, int price) {
        if(!restaurantExists(restaurantId)) {
            throw new NoSuchElementException("Restaurant not found: " + restaurantId);
        }
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");

        Item item = new Item(nextItemId(), itemName, price, ItemStatus.AVAILABLE);
        menuRepository.saveItem(item, restaurantId, categoryId);
        menuCache.invalidate(restaurantId);
        return item.id;
    }

    private String nextItemId() {
        return "I" + seq.getAndIncrement();
    }

    void updateItemName(String restaurantId, String categoryId, String itemId, String newName) {
        if(!restaurantExists(restaurantId)) {
            throw new NoSuchElementException("Restaurant not found: " + restaurantId);
        }
        Item item = loadItem(restaurantId, categoryId, itemId);
        item.name = newName;
        menuRepository.saveItem(item, restaurantId, categoryId);
        menuCache.invalidate(restaurantId);
    }

    public void changePrice(String restaurantId, String categoryId, String itemId, double newPrice) {
        if(!restaurantExists(restaurantId)) {
            throw new NoSuchElementException("Restaurant not found: " + restaurantId);
        }
        if (newPrice < 0) throw new IllegalArgumentException("Price cannot be negative");
        Item item = loadItem(restaurantId, categoryId, itemId);
        item.price = newPrice;

        menuRepository.saveItem(item, restaurantId, categoryId);
        menuCache.invalidate(restaurantId);
    }

    public void toggleAvailability(String restaurantId, String categoryId, String itemId) {
        if(!restaurantExists(restaurantId)) {
            throw new NoSuchElementException("Restaurant not found: " + restaurantId);
        }
        Item item = loadItem(restaurantId, categoryId, itemId);
        item.status = item.status == ItemStatus.AVAILABLE ? ItemStatus.UNAVAILABLE : ItemStatus.AVAILABLE;

        menuRepository.saveItem(item, restaurantId, categoryId);
        menuCache.invalidate(restaurantId);
    }

    public void deleteItem(String restaurantId, String categoryId, String itemId) {
        if(!restaurantExists(restaurantId)) {
            throw new NoSuchElementException("Restaurant not found: " + restaurantId);
        }

        menuRepository.deleteItem(restaurantId, categoryId, itemId);
        menuCache.invalidate(restaurantId);
    }

    private Item loadItem(String restaurantId, String categoryId, String itemId) {
        return menuRepository.getItem(restaurantId, categoryId, itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found: " + itemId));
    }
}
