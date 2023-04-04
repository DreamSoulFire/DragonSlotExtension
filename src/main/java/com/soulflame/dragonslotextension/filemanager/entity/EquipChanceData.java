package com.soulflame.dragonslotextension.filemanager.entity;

import java.util.List;

public class EquipChanceData {

    private final String identifier;
    private final List<String> slotList;
    private final List<String> commands;
    public EquipChanceData(String identifier, List<String> slotList, List<String> commands) {
        this.identifier = identifier;
        this. slotList = slotList;
        this.commands = commands;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getSlotList() {
        return slotList;
    }

    public List<String> getCommands() {
        return commands;
    }
}
