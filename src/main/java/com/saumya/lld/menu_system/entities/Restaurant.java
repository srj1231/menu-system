package com.saumya.lld.menu_system.entities;

import lombok.Getter;

@Getter
public class Restaurant {
    public final String id;
    public String name;

    public Restaurant(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
