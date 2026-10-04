package com.saumya.lld.menu_system.services;

import com.saumya.lld.menu_system.entities.Restaurant;
import com.saumya.lld.menu_system.store.MenuRepository;
import com.saumya.lld.menu_system.store.RestaurantRepository;

import java.util.concurrent.atomic.AtomicInteger;

public class RestaurantService {

    private final RestaurantRepository repo;
    private final MenuRepository menuRepository;
    private final AtomicInteger sequentialId = new AtomicInteger(1);

    public RestaurantService(RestaurantRepository repo, MenuRepository menuRepository) {
        this.repo = repo;
        this.menuRepository = menuRepository;
    }

    public String createRestaurant(String name) {
        Restaurant r = new Restaurant("R" + sequentialId.getAndIncrement(), name);
        repo.save(r);
        menuRepository.saveRestaurant(r);
        return r.id;
    }
}
