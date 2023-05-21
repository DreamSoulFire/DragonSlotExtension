package com.soulflame.dragonslotextension.utils;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CommandUtil {

    /**
     * 构建指令
     * @param sender 指令执行者
     * @param commands 指令
     * @param check 是否为控制台执行
     */
    public static void build(CommandSender sender, String commands, String check) {
        Player player = (Player) sender;
        commands = TextUtil.replaceName(player, commands);
        commands = PlaceholderAPI.setPlaceholders(player, commands);
        if (check.equalsIgnoreCase("console"))
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands);
        else if (check.equalsIgnoreCase("player"))
            Bukkit.dispatchCommand(player, commands);
        else TextUtil.sendMessage(DragonSlotExtension.message.fileError);
    }

    /**
     * 判断指令执行类型
     * @param sender 玩家
     * @param command 指令
     * @param index 下标
     */
    public static void run(CommandSender sender, String[] command, int index) {
        String cmd = command[index - 1];
        String check = command[index - 2];
        build(sender, cmd, check);
    }

    /**
     * 检测配置文件指令列表的长度
     * @param sender 文件
     * @param command 指令列表
     */
    public static void run(CommandSender sender, String[] command) {
        if (command.length != 2 && command.length != 3) {
            TextUtil.sendMessage(DragonSlotExtension.message.fileError);
            return;
        }
        run(sender, command, command.length);
    }

    /**
     * 为玩家执行指令
     * @param player 玩家
     * @param commands 指令列表
     */
    public static void run(Player player, List<String> commands, int amount, ItemMeta meta) {
        String name = meta.hasDisplayName() ? meta.getDisplayName() : meta.getLocalizedName();
        commands.forEach(cmd -> {
            cmd = cmd.replace("<amount>", String.valueOf(amount)).replace("<item>", name);
            if (!cmd.contains("<->")) {
                TextUtil.sendMessage(DragonSlotExtension.message.fileError);
                return;
            }
            String[] cmdSplit = cmd.split("<->");
            if (cmdSplit.length != 2) {
                TextUtil.sendMessage(DragonSlotExtension.message.fileError);
                return;
            }
            CommandUtil.run(player, cmdSplit);
        });
    }

    /**
     * 运行指令
     * @param sender 玩家
     * @param commands 指令
     * @param identifier 指令执行标识
     */
    public static void run(CommandSender sender, String commands, String identifier) {
        if (!commands.contains("<->")) {
            TextUtil.sendMessage(DragonSlotExtension.message.fileError);
            return;
        }
        String[] command = commands.split("<->");
        if (command.length != 3) return;
        if (identifier.equalsIgnoreCase("success")) run(sender, command);
        else if (identifier.equalsIgnoreCase("fail")) run(sender, command);
        else TextUtil.sendMessage(DragonSlotExtension.message.fileError);
    }

    /**
     * 运行指令
     * @param sender 玩家
     * @param commands 指令列表
     * @param identifier 指令执行标识
     */
    public static void run(CommandSender sender, List<String> commands, String itemName,String identifier) {
        commands.forEach(cmd -> {
            cmd = cmd.replace("<item>", itemName);
            run(sender, cmd, identifier);
        });
    }
}
