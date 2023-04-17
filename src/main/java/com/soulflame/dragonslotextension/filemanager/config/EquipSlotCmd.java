package com.soulflame.dragonslotextension.filemanager.config;

import com.soulflame.dragonslotextension.filemanager.FileManager;
import com.soulflame.dragonslotextension.filemanager.entity.EquipSlotCmdData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipSlotCmd extends FileManager {

    public Map<String, EquipSlotCmdData> map;

    /**
     * 读取配置文件
     *
     * @param folder 配置文件夹
     * @param name   配置文件名
     */
    public EquipSlotCmd(File folder, String name) {
        super(folder, name);
    }

    @Override
    protected void loadData() {
        map = new HashMap<>();
        YamlConfiguration yaml = getYaml();
        ConfigurationSection keys = yaml.getConfigurationSection("");
        keys.getKeys(false).forEach(key -> {
            ConfigurationSection section = keys.getConfigurationSection(key);
            List<String> check = section.getStringList("check");
            List<String> commands = section.getStringList("commands");
            EquipSlotCmdData data = new EquipSlotCmdData(check, commands);
            map.put(key, data);
        });
    }
}
