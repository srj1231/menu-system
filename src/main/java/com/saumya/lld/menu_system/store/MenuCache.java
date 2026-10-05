package com.saumya.lld.menu_system.store;

import com.saumya.lld.menu_system.entities.Menu;

import java.util.Optional;

public interface MenuCache {

    Optional<Menu> getMenu(String restaurantId);
    void putMenu(String restaurantId, Menu menu);
    void invalidate(String restaurantId);
}
