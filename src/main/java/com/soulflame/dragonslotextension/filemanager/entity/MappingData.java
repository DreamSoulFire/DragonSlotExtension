package com.soulflame.dragonslotextension.filemanager.entity;

import lombok.Getter;

import java.util.List;

public class MappingData {

    @Getter
    private final String dragonSlots;
    @Getter
    private final String vanillaSlots;
    @Getter
    private final List<String> lore;

    public MappingData(String dragon_slot, String vanilla_slot, List<String> lore) {
        this.dragonSlots = dragon_slot;
        this.vanillaSlots = vanilla_slot;
        this.lore = lore;
    }
}
