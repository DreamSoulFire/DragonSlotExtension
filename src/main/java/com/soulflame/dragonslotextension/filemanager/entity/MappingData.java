package com.soulflame.dragonslotextension.filemanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MappingData {

    private final String dragonSlots;

    private final String vanillaSlots;

    private final List<String> lore;

}
