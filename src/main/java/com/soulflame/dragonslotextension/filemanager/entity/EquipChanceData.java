package com.soulflame.dragonslotextension.filemanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EquipChanceData {

    private final String identifier;

    private final List<String> slotList;

    private final List<String> commands;

}
