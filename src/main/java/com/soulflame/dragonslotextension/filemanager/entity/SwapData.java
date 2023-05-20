package com.soulflame.dragonslotextension.filemanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SwapData {


    private final String key;

    private final List<String> slots;

}
