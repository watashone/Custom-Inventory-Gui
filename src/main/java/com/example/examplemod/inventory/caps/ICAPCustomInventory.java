package com.example.examplemod.inventory.caps;

import com.example.examplemod.inventory.container.CustomInventory;

public interface ICAPCustomInventory {

    public void copyInventory(ICAPCustomInventory inventory);
    public CustomInventory getInventory();
}
