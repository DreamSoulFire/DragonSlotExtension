package com.soulflame.dragonslotextension;

import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.Bukkit;

public class Hooker {
    public Hooker(String plugin) {
        if (Bukkit.getPluginManager().getPlugin(plugin) == null) {
            TextUtil.sendMessage("&c未检测到 " + plugin + "插件, 插件即将关闭");
            Bukkit.getPluginManager().disablePlugin(DragonSlotExtension.getPlugin());
            return;
        }
        TextUtil.sendMessage("&a已检测到 " + plugin + " 插件");
    }
}
