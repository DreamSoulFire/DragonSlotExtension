package com.soulflame.dragonslotextension.filemanager.entity;

import java.util.List;

public class MappingData {
    private final String dragonSlots;
    private final String vanillaSlots;
    private final List<String> lore;

    public MappingData(String dragon_slot, String vanilla_slot, List<String> lore) {
        this.dragonSlots = dragon_slot;
        this.vanillaSlots = vanilla_slot;
        this.lore = lore;
    }

    public String getDragonSlots() {
        return dragonSlots;
    }

    public String getVanillaSlots() {
        return vanillaSlots;
    }

    public List<String> getLore() {
        return lore;
    }
}
