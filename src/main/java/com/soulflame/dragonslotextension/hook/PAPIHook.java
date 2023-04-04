package com.soulflame.dragonslotextension.hook;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.Bukkit;

public class PAPIHook {
    public static boolean hook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            TextUtil.sendMessage("&c未检测到 PlaceholderAPI 插件, 插件即将关闭");
            DragonSlotExtension.getPlugin().onDisable();
            return false;
        }
        TextUtil.sendMessage("&a已检测到 PlaceholderAPI 插件");
        return true;
    }
}
