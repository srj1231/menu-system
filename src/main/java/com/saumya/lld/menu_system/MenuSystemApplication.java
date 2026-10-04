package com.saumya.lld.menu_system;

import com.saumya.lld.menu_system.services.MenuService;
import com.saumya.lld.menu_system.services.RestaurantService;
import com.saumya.lld.menu_system.store.InMemoryMenuCache;
import com.saumya.lld.menu_system.store.InMemoryMenuRepository;
import com.saumya.lld.menu_system.store.InMemoryRestaurantRepository;
import com.saumya.lld.menu_system.store.RestaurantRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.NoSuchElementException;

@SpringBootApplication
public class MenuSystemApplication {

	public static void main(String[] args) {
		RestaurantRepository restaurantRepo = new InMemoryRestaurantRepository();
		InMemoryMenuRepository menuRepo = new InMemoryMenuRepository();
		RestaurantService restaurantSvc = new RestaurantService(restaurantRepo, menuRepo);
		MenuService menuService = new MenuService(menuRepo, new InMemoryMenuCache(), restaurantRepo);

		String r = restaurantSvc.createRestaurant("Pizza Hut");

		String pizza = menuService.createCategory(r, "Pizza");
		String sides = menuService.createCategory(r, "Sides");
		String desserts = menuService.createCategory(r, "Desserts");
		String drinks = menuService.createCategory(r, "Drinks");

		String margherita = menuService.createItem(r, pizza, "Margherita", 299);
		menuService.createItem(r, pizza, "Farmhouse", 399);
		menuService.createItem(r, sides, "Garlic Bread", 149);
		String wings = menuService.createItem(r, sides, "Wings", 249);
		menuService.createItem(r, desserts, "Brownie", 129);
		menuService.createItem(r, drinks, "Coke", 129);

		menuService.toggleAvailability(r, sides, wings);   // Wings -> UNAVAILABLE

		System.out.println(">> First read (cache MISS)");
		menuService.getMenu(r).print();

		System.out.println("\n>> Second read (cache HIT)");
		menuService.getMenu(r).print();
		System.out.println("hits=" + menuService.cacheHits + " misses=" + menuService.cacheMisses);

		System.out.println("\n>> Change Margherita price to 349 (invalidates cache)");
		menuService.changePrice(r, pizza, margherita, 349);

		System.out.println("\n>> Read after write (cache MISS, fresh data)");
		menuService.getMenu(r).print();
		System.out.println("hits=" + menuService.cacheHits + " misses=" + menuService.cacheMisses);

		System.out.println("\n>> Rename category, delete item, delete category");
		menuService.updateCategory(r, desserts, "Sweets");
		menuService.deleteItem(r, drinks, menuService.getMenu(r).categories.get(drinks).items.keySet().iterator().next());
		menuService.deleteCategory(r, drinks);
		menuService.getMenu(r).print();

		System.out.println("\n>> Error handling");
		try { menuService.changePrice(r, pizza, "bad-id", 100); }
		catch (NoSuchElementException e) { System.out.println("Caught: " + e.getMessage()); }
		try { menuService.createItem(r, pizza, "Free Pizza", -5); }
		catch (IllegalArgumentException e) { System.out.println("Caught: " + e.getMessage()); }
		try { menuService.getMenu("R999"); }
		catch (NoSuchElementException e) { System.out.println("Caught: " + e.getMessage()); }

		SpringApplication.run(MenuSystemApplication.class, args);
	}

}
