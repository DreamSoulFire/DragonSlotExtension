package com.soulflame.dragonslotextension.utils;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandUtil {

    /**
     * 构建指令
     * @param sender 指令执行者
     * @param commands 指令
     * @param isConsole 是否为控制台执行
     */
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

    /**
     * 判断指令执行类型
     * @param sender 玩家
     * @param command 指令
     * @param index 下标
     */
    public static void run(CommandSender sender, String[] command,int index) {
        if ("console".equalsIgnoreCase(command[index - 2])) build(sender, command[index - 1], true);
        else if ("player".equalsIgnoreCase(command[index - 2])) build(sender, command[index - 1], false);
        else TextUtil.sendMessage(DragonSlotExtension.message.fileError);
    }

    /**
     * 检测配置文件指令列表的长度
     * @param sender 文件
     * @param command 指令列表
     */
    public static void run(CommandSender sender, String[] command) {
        switch (command.length) {
            case 2:
            case 3:
                run(sender, command, command.length);
                break;
            default:
                TextUtil.sendMessage(DragonSlotExtension.message.fileError);
        }
    }

    /**
     * 为玩家执行指令
     * @param player 玩家
     * @param commands 指令列表
     */
    public static void run(Player player, List<String> commands, int amount) {
        commands.forEach(cmd -> {
            cmd = cmd.replace("<amount>", String.valueOf(amount));
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
     * @param isSuccess 是否在成功时运行
     */
    public static void run(CommandSender sender, String commands, boolean isSuccess) {
        if (!commands.contains("<->")) {
            TextUtil.sendMessage(DragonSlotExtension.message.fileError);
            return;
        }
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
                TextUtil.sendMessage(DragonSlotExtension.message.fileError);
                break;
        }
    }

    /**
     * 运行指令
     * @param sender 玩家
     * @param commands 指令列表
     * @param isSuccess 是否在成功时运行
     */
    public static void run(CommandSender sender, List<String> commands, boolean isSuccess) {
        commands.forEach(cmd -> run(sender, cmd, isSuccess));
    }
}
