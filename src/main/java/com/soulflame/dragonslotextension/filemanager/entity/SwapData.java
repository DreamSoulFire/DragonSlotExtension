package com.soulflame.dragonslotextension.filemanager.entity;

import java.util.List;

public class SwapData {

    private final String key;
    private final List<String> slots;

    public SwapData(String key, List<String> slots) {
        this.key = key;
        this.slots = slots;
    }

    public String getKey() {
        return key;
    }

    public List<String> getSlots() {
        return slots;
    }
}
