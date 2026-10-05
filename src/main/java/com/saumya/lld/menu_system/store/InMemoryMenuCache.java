package com.saumya.lld.menu_system.store;

import com.saumya.lld.menu_system.entities.Menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryMenuCache implements MenuCache {

    private final Map<String, Menu> cacheStore = new HashMap<>();
    private String cacheKey(String restaurantId) { return "menu:" + restaurantId; }

    @Override
    public Optional<Menu> getMenu(String restaurantId) {
        return Optional.ofNullable(cacheStore.get(cacheKey(restaurantId)));
    }

    @Override
    public void putMenu(String restaurantId, Menu menu) {
        cacheStore.put(cacheKey(restaurantId), menu);
    }

    @Override
    public void invalidate(String restaurantId) {
        cacheStore.remove(cacheKey(restaurantId));
    }
}
