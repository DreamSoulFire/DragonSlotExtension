package com.soulflame.dragonslotextension.filemanager.config;

import com.soulflame.dragonslotextension.filemanager.FileManager;

import java.io.File;

public class SwapSlot extends FileManager {
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
    protected void loadData() {

    }
}
