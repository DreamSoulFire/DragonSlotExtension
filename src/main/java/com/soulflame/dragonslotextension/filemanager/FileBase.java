package com.soulflame.dragonslotextension.filemanager;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public interface FileBase {
    void create(File folder, String name);
    void reload(File folder, String name);
    File getFile();
    YamlConfiguration getYaml();
}
