package com.saumya.lld.menu_system.store;

import com.saumya.lld.menu_system.entities.Restaurant;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRestaurantRepository implements RestaurantRepository {

    private final Map<String, Restaurant> restaurants = new HashMap<>();

    public void save(Restaurant r) {
        restaurants.put(r.id, r);
    }
    public boolean exists(String id) {
        return restaurants.containsKey(id);
    }
}
