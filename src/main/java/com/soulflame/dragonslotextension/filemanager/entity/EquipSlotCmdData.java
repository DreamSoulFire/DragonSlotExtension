package com.soulflame.dragonslotextension.filemanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EquipSlotCmdData {


    private final List<String> check;

    private final List<String> commands;

}