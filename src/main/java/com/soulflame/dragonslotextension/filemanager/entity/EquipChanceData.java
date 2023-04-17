package com.soulflame.dragonslotextension.filemanager.entity;

import lombok.Getter;

import java.util.List;

public class EquipChanceData {

    @Getter
    private final String identifier;
    @Getter
    private final List<String> slotList;
    @Getter
    private final List<String> commands;

    public EquipChanceData(String identifier, List<String> slotList, List<String> commands) {
        this.identifier = identifier;
        this. slotList = slotList;
        this.commands = commands;
    }
}
