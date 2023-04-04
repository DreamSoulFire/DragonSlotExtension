package com.soulflame.dragonslotextension.filemanager;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class FileManager implements FileBase {

    private File file;
    private YamlConfiguration yaml;
    private final String name;

    public String getName() {
        return name;
    }

    /**
     * 读取配置文件
     * @param folder 配置文件夹
     * @param name 配置文件名
     */
    public FileManager(File folder, String name) {
        this.name = name;
        reload(folder, name);
    }

    /**
     * 获取配置文件的yaml
     * @return yaml
     */
    @Override
    public YamlConfiguration getYaml() {
        return yaml;
    }

    /**
     * 获取配置文件
     * @return file
     */
    @Override
    public File getFile() {
        return file;
    }

    /**
     * 创建配置文件
     * @param folder 配置文件夹
     * @param name 配置文件名
     */
    @Override
    public void create(File folder, String name) {
        file = new File(folder, name);
        if (file.exists()) TextUtil.sendMessage("&a已检测到 " + getName() + " 文件");
        else {
            TextUtil.sendMessage("&4未检测到 " + getName() + " 文件,正在生成...");
            DragonSlotExtension.getPlugin().saveResource(name, false);
        }
        yaml = new YamlConfiguration();
        try {
            yaml.load(getFile());
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 重载配置文件
     * @param folder 配置文件夹
     * @param name 配置文件名
     */
    @Override
    public void reload(File folder, String name) {
        create(folder, name);
        loadData();
    }

    /**
     * 加载配置文件数据
     */
    protected abstract void loadData();
}
