package com.soulflame.dragonslotextension.filemanager.config;

import com.soulflame.dragonslotextension.filemanager.FileManager;
import com.soulflame.dragonslotextension.filemanager.entity.SwapData;
import eos.moe.dragoncore.api.CoreAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class SwapSlot extends FileManager {

    public Map<String, SwapData> map;

    /**
     * 读取配置文件
     *
     * @param folder 配置文件夹
     * @param name   配置文件名
     */
    public SwapSlot(File folder, String name) {
        super(folder, name);
    }

    @Override
    public void loadData() {
        map = new HashMap<>();
        YamlConfiguration yaml = getYaml();
        ConfigurationSection keys = yaml.getConfigurationSection("");
        keys.getKeys(false).forEach(key -> {
            ConfigurationSection section = keys.getConfigurationSection(key);
            String changeKey = section.getString("exchange-key", "");
            CoreAPI.registerKey(changeKey);
            List<String> slots = section.getStringList("slot-list");
            SwapData data = new SwapData(changeKey, slots);
            map.put(key, data);
        });
    }
}
