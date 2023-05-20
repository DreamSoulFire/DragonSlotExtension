package com.soulflame.dragonslotextension.utils;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TextUtil {
    /**
     * 解析 & 字符的信息发送方法
     * @param sender 发送信息的玩家
     * @param message 发送的信息
     */
    public static void sendMessage(CommandSender sender, List<String> message) {
        message.forEach(msg -> sendMessage(sender, replaceColor(msg)));
    }
    /**
     * 解析 & 字符的信息发送方法
     * @param sender 发送信息的玩家
     * @param message 发送的信息
     */
    public static void sendNoPrefixMessage(CommandSender sender, List<String> message) {
        message.forEach(msg -> sendNoPrefixMessage(sender, replaceColor(msg)));
    }

    /**
     * 解析 & 字符的信息发送方法
     * @param sender 发送信息的玩家
     * @param message 发送的信息
     */
    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(replaceColor(DragonSlotExtension.message.prefix + message));
    }

    /**
     * 解析 & 字符的信息发送方法
     * @param sender 发送信息的玩家
     * @param message 发送的信息
     */
    public static void sendNoPrefixMessage(CommandSender sender, String message) {
        sender.sendMessage(replaceColor(message));
    }

    /**
     * 解析 & 字符的后台信息发送方法 [有插件前缀]
     * @param message 发送的信息
     */
    public static void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(replaceColor(DragonSlotExtension.pluginPrefix + message));
    }

    /**
     * 解析 & 字符的后台信息发送方法 [无插件前缀]
     * @param message 发送的信息
     */
    public static void sendNoPrefixMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(replaceColor(message));
    }

    /**
     * 解析 & 字符
     * @param message 文本
     * @return message
     */
    public static String replaceColor(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    /**
     * 替换玩家变量
     * @param sender 发送者
     * @param message 信息
     * @return 替换后的信息
     */
    public static String replaceName(CommandSender sender, String message) {
        message = message.replace("<player>", sender.getName());
        return message;
    }

}
