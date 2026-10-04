package com.saumya.lld.menu_system.store;

import com.saumya.lld.menu_system.entities.Restaurant;

public interface RestaurantRepository {

    void save(Restaurant restaurant);
    boolean exists(String restaurantId);
}
