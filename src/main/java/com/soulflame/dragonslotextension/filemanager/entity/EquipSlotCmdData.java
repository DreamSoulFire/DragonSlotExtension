package com.soulflame.dragonslotextension.filemanager.entity;

import lombok.Getter;

import java.util.List;

public class EquipSlotCmdData {

    @Getter
    private final List<String> check;
    @Getter
    private final List<String> commands;

    public EquipSlotCmdData(List<String> check, List<String> commands) {
        this.check = check;
        this.commands = commands;
    }
}
