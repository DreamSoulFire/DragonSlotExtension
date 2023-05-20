package com.soulflame.dragonslotextension.filemanager.config;

import com.soulflame.dragonslotextension.filemanager.FileManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class ConfigFile extends FileManager {
    public boolean debugEquipChance;
    public boolean debugSwapItem;
    public String equipMode;
    public String equipViewSlot;
    public int equipTakeTime;
    public List<String> saveItems;

    public ConfigFile(File folder, String name) {
        super(folder, name);
    }

    @Override
    protected void loadData() {
        YamlConfiguration yaml = getYaml();
        debugEquipChance = yaml.getBoolean("debug.equip-chance", false);
        debugSwapItem = yaml.getBoolean("debug.swap-slot", false);
        equipMode = yaml.getString("global-setting.equip-chance.mode", "normal");
        equipViewSlot = yaml.getString("global-setting.equip-chance.view-slot", "展示槽位");
        equipTakeTime = yaml.getInt("global-setting.equip-chance.take-time", 40);
        saveItems = yaml.getStringList("global-setting.equip-chance.save-item");
    }
}
