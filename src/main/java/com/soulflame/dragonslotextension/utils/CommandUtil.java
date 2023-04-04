package com.soulflame.dragonslotextension.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandUtil {
    public static void build(CommandSender sender, String commands, boolean isConsole) {
        Player player = (Player) sender;
        commands = TextUtil.replaceName(player, commands);
        commands = PlaceholderAPI.setPlaceholders(player, commands);
        if (isConsole) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands);
            return;
        }
        Bukkit.dispatchCommand(player, commands);
    }

    public static void run(CommandSender sender, String[] command) {
        if ("console".equalsIgnoreCase(command[1])) build(sender, command[2], true);
        else if ("player".equalsIgnoreCase(command[1])) build(sender, command[2], false);
        else TextUtil.sendMessage(sender, "&c该执行类型不存在, 请检查配置");
    }

    public static void run(CommandSender sender, String commands, boolean isSuccess) {
        if (!commands.contains("<->")) return;
        String[] command = commands.split("<->");
        if (command.length != 3) return;
        switch (command[0]) {
            case "success":
                if (!isSuccess) return;
                run(sender, command);
                break;
            case "fail":
                if (isSuccess) return;
                run(sender, command);
                break;
            default:
                TextUtil.sendMessage(sender, "&c该判断不存在, 请检查配置");
                break;
        }
    }

    public static void run(CommandSender sender, List<String> commands, boolean isSuccess) {
        commands.forEach(cmd -> run(sender, cmd, isSuccess));
    }
}
