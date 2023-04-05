package com.soulflame.dragonslotextension.commands.subcommand;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.commands.CommandBase;
import com.soulflame.dragonslotextension.utils.ItemUtil;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Clear extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        if (player != null) {
            ItemUtil.removeItemInPlayer(player);
            return;
        }
        TextUtil.sendMessage(sender, DragonSlotExtension.message.playerOffline);
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "dse.command.clear";
    }

    @Override
    public String getCommandDesc() {
        return "/dse clear 玩家";
    }
}
