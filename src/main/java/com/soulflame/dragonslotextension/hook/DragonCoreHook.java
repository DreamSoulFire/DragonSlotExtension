package com.soulflame.dragonslotextension.hook;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.Bukkit;

public class DragonCoreHook {
    public static boolean hook() {
        if (Bukkit.getPluginManager().getPlugin("DragonCore") == null) {
            TextUtil.sendMessage("&c未检测到 DragonCore 插件, 插件即将关闭");
            DragonSlotExtension.getPlugin().onDisable();
            return false;
        }
        TextUtil.sendMessage("&a已检测到 DragonCore 插件");
        return true;
    }
}
