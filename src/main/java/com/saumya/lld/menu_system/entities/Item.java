package com.saumya.lld.menu_system.entities;

import com.saumya.lld.menu_system.enums.ItemStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Item {
    public final String id;
    public String name;
    public double price;
    public ItemStatus status;

    // item copy for caching, immutability snapshot, concurrent safety
    //  ensures that cached menu data remains consistent and isn't accidentally modified by consumers
    public Item copy() {
        return new Item(id, name, price, status);
    }

    @Override
    public String toString() {
        return "Item [id=" + id + ", name=" + name + ", price=" + price + ", status=" + status + "]";
    }
}
