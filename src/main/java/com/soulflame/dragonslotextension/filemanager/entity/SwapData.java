package com.soulflame.dragonslotextension.filemanager.entity;

import lombok.Getter;

import java.util.List;

public class SwapData {

    @Getter
    private final String key;
    @Getter
    private final List<String> slots;

    public SwapData(String key, List<String> slots) {
        this.key = key;
        this.slots = slots;
    }
}
