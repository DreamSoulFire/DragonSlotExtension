package com.soulflame.dragonslotextension.filemanager;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.utils.TextUtil;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public abstract class FileManager implements FileBase {

    @Getter
    private File file;
    @Getter
    private YamlConfiguration yaml;

    /**
     * 读取配置文件
     * @param folder 配置文件夹
     * @param name 配置文件名
     */
    public FileManager(File folder, String name) {
        reload(folder, name);
    }

    /**
     * 创建配置文件
     * @param folder 配置文件夹
     * @param name 配置文件名
     */
    @Override
    public void create(File folder, String name) {
        file = new File(folder, name);
        if (!file.exists()) {
            TextUtil.sendMessage("&4未检测到 " + name + " 文件,正在生成...");
            DragonSlotExtension.getPlugin().saveResource(name, false);
        } else {
            TextUtil.sendMessage("&a已检测到 " + name + " 文件");
        }
        yaml = YamlConfiguration.loadConfiguration(file);
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
