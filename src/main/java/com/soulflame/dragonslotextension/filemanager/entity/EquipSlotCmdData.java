package com.soulflame.dragonslotextension.filemanager.entity;

import java.util.List;

public class EquipSlotCmdData {

    private final String check;
    private final List<String> slots;
    private final List<String> commands;

    public EquipSlotCmdData(String check, List<String> slots, List<String> commands) {
        this.check = check;
        this.slots = slots;
        this.commands = commands;
    }

    public List<String> getSlots() {
        return slots;
    }

    public String getCheck() {
        return check;
    }

    public List<String> getCommands() {
        return commands;
    }
}
