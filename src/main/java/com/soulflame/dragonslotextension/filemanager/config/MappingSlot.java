package com.soulflame.dragonslotextension.filemanager.config;

import com.soulflame.dragonslotextension.filemanager.FileManager;
import com.soulflame.dragonslotextension.filemanager.entity.MappingData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingSlot extends FileManager {
    public Map<String, MappingData> mappingMap;

    /**
     * 读取配置文件
     *
     * @param folder 配置文件夹
     * @param name   配置文件名
     */
    public MappingSlot(File folder, String name) {
        super(folder, name);
    }

    @Override
    protected void loadData() {
        mappingMap = new HashMap<>();
        YamlConfiguration yaml = getYaml();
        ConfigurationSection keys = yaml.getConfigurationSection("");
        for (String key : keys.getKeys(false)) {
            ConfigurationSection section = keys.getConfigurationSection(key);
            String dragonSlots = section.getString("dragon-core", "");
            String vanillaSlots = section.getString("vanilla", "");
            List<String> lores = section.getStringList("identifier-lore");
            MappingData data = new MappingData(dragonSlots, vanillaSlots, lores);
            mappingMap.put(key, data);
        }
    }
}
