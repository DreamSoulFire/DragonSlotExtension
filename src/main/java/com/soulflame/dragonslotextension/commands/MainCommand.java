package com.soulflame.dragonslotextension.commands;

import com.google.common.collect.Maps;
import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.commands.subcommand.*;
import com.soulflame.dragonslotextension.filemanager.config.MessageFile;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainCommand implements TabExecutor {
    private final Map<String, CommandBase> commandMap;

    public MainCommand() {
        commandMap = Maps.newHashMap();
        registerCommand(new ChangeSlot());
        registerCommand(new GetSlot());
        registerCommand(new Help());
        registerCommand(new Multi());
        registerCommand(new Open());
        registerCommand(new Reload());
        registerCommand(new RemoveSlot());
        registerCommand(new SetSlot());
    }

    private void registerCommand(CommandBase commandBase) {
        commandMap.put(commandBase.getCommand(), commandBase);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessageFile message = DragonSlotExtension.message;
        if (args.length == 0) {
            if (!sender.isOp() && !sender.hasPermission("dse.command.help")) return true;
            TextUtil.sendNoPrefixMessage(sender, message.help);
            return true;
        }
        CommandBase commandBase = commandMap.get(args[0].toLowerCase());
        if (commandBase == null) {
            String noChild = message.getYaml().getString("command.no-child-command", "&c该指令不存在: <command>");
            noChild = noChild.replace("<command>", args[0]);
            TextUtil.sendMessage(sender, noChild);
            return true;
        }
        if (!sender.hasPermission(commandBase.getPermission())) {
            String perm = message.getYaml().getString("command.dont-have-permission", "&c执行该指令需要权限: <permission>");
            perm = perm.replace("<permission>", commandBase.getPermission());
            TextUtil.sendMessage(sender, perm);
            return true;
        }
        if (commandBase.getLength() > args.length) {
            String error = message.getYaml().getString("command.command-args-error", "&c指令参数错误或不完整,请检查是否输错了指令: <command>");
            error = error.replace("<command>", commandBase.getCommandDesc());
            TextUtil.sendMessage(sender, error);
            return true;
        }
        String[] strings = Arrays.copyOfRange(args, 1, args.length);
        if (!(sender instanceof Player)) {
            commandBase.onConsoleCommand(sender, strings);
            return true;
        }
        commandBase.onPlayerCommand((Player) sender, strings);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return commandMap.keySet().stream().filter(cmd -> cmd.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        } else if (args.length >= 2) {
            return commandMap.containsKey(args[0].toLowerCase()) ? commandMap.get(args[0].toLowerCase()).
                    onTabComplete(sender, command, label, args) : null;
        }
        return null;
    }
}
