package com.soulflame.dragonslotextension;

import com.soulflame.dragonslotextension.commands.MainCommand;
import com.soulflame.dragonslotextension.filemanager.config.*;
import com.soulflame.dragonslotextension.hook.DragonCoreHook;
import com.soulflame.dragonslotextension.hook.PAPIHook;
import com.soulflame.dragonslotextension.listener.equip.EquipChanceGui;
import com.soulflame.dragonslotextension.listener.equip.EquipChanceNormal;
import com.soulflame.dragonslotextension.listener.equip.EquipCommand;
import com.soulflame.dragonslotextension.listener.mapping.AntiAction;
import com.soulflame.dragonslotextension.listener.mapping.EquipItem;
import com.soulflame.dragonslotextension.listener.swap.SwapItem;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DragonSlotExtension extends JavaPlugin {

    public static String pluginPrefix = "&7[&6Dragon&bSlot&eExtension&7] ";
    private static DragonSlotExtension plugin;
    public static ConfigFile config;
    public static EquipChance equipChance;
    public static EquipSlotCmd equipCommand;
    public static EquipGui equipChanceGui;
    public static MappingSlot mappingSlot;
    public static Message message;
    public static SwapSlot swapSlot;

    public static DragonSlotExtension getPlugin() {
        return plugin;
    }

    public static void saveAllConfig() {
        File folder = plugin.getDataFolder();
        config = new ConfigFile(folder, "config.yml");
        equipChance = new EquipChance(folder, "modules/equip-chance.yml");
        equipCommand = new EquipSlotCmd(folder, "modules/equip-command.yml");
        equipChanceGui = new EquipGui(folder, "gui/equip-chance-gui.yml");
        mappingSlot = new MappingSlot(folder, "modules/mapping.yml");
        message = new Message(folder, "message.yml");
        swapSlot = new SwapSlot(folder, "modules/swap.yml");
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EquipCommand(), this);
        Bukkit.getPluginManager().registerEvents(new EquipChanceGui(), this);
        Bukkit.getPluginManager().registerEvents(new EquipChanceNormal(), this);
        Bukkit.getPluginManager().registerEvents(new AntiAction(), this);
        Bukkit.getPluginManager().registerEvents(new EquipItem(), this);
        Bukkit.getPluginManager().registerEvents(new SwapItem(), this);
    }

    @Override
    public void onEnable() {
        plugin = this;
        long start = System.currentTimeMillis();
        TextUtil.sendNoPrefixMessage("&b====================================================");
        TextUtil.sendMessage("&a插件已启动, 开始构建插件");
        saveAllConfig();
        if (!DragonCoreHook.hook() || !PAPIHook.hook()) return;
        TextUtil.sendMessage("&6开始注册事件");
        registerEvents();
        TextUtil.sendMessage("&6开始注册指令");
        Bukkit.getPluginCommand("dragonslotextension").setExecutor(new MainCommand());
        Bukkit.getPluginCommand("dragonslotextension").setTabCompleter(new MainCommand());
        long finish = System.currentTimeMillis();
        finish -= start;
        TextUtil.sendMessage("&b构建用时: " + finish + "ms");
        TextUtil.sendNoPrefixMessage("&b====================================================");
    }

    @Override
    public void onDisable() {
        TextUtil.sendNoPrefixMessage("&b====================================================");
        TextUtil.sendMessage("&4插件已关闭");
        TextUtil.sendNoPrefixMessage("&b====================================================");
    }

}