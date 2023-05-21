package com.soulflame.dragonslotextension.filemanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChangeLoreData {

    private String slot;

    private List<String> plans;

}
