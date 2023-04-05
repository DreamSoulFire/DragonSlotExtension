package com.soulflame.dragonslotextension.commands.subcommand;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.commands.CommandBase;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        TextUtil.sendNoPrefixMessage(sender, DragonSlotExtension.message.help);
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "dse.command.help";
    }

    @Override
    public String getCommandDesc() {
        return "/dse help";
    }
}
