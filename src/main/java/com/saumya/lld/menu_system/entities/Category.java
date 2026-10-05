package com.saumya.lld.menu_system.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Category {

    public final String id;
    public String name;
    public final Map<String, Item> items = new LinkedHashMap<>();
    // LinkedHashMap keeps insertion order and still gives O(1) lookup by id.
    // TreeMap sorts by key, and string ids sort lexicographically ("I10" comes before "I2")
    // List<Item>: every update, toggle, or delete by id would be an O(n)

    public Category copy() {
        Category c = new Category(id, name);
        items.values().forEach(item -> c.items.put(item.id, item.copy()));
        return c;
    }
}
