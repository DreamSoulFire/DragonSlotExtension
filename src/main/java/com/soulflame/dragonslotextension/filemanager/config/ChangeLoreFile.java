package com.soulflame.dragonslotextension.filemanager.config;

import com.soulflame.dragonslotextension.filemanager.FileManager;
import com.soulflame.dragonslotextension.filemanager.entity.ChangeLoreData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeLoreFile extends FileManager {

    public Map<String, ChangeLoreData> map;

    /**
     * 读取配置文件
     *
     * @param folder 配置文件夹
     * @param name   配置文件名
     */
    public ChangeLoreFile(File folder, String name) {
        super(folder, name);
    }

    @Override
    protected void loadData() {
        map = new HashMap<>();
        YamlConfiguration yaml = getYaml();
        ConfigurationSection section = yaml.getConfigurationSection("");
        for (String key : section.getKeys(false)) {
            section = section.getConfigurationSection(key);
            String slot = section.getString("slot");
            List<String> plans = section.getStringList("lore-plans");
            ChangeLoreData data = new ChangeLoreData(slot, plans);
            map.put(key, data);
        }
    }
}
