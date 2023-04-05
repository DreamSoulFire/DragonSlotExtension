package com.soulflame.dragonslotextension.commands.subcommand;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.commands.CommandBase;
import com.soulflame.dragonslotextension.utils.SlotUtil;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetSlot extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        TextUtil.sendMessage(sender, DragonSlotExtension.message.cantUseInConsole);
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            SlotUtil.getSlot(player, target, args[1], Boolean.parseBoolean(args[2]));
            return;
        }
        TextUtil.sendMessage(player, DragonSlotExtension.message.playerOffline);
    }

    @Override
    public String getPermission() {
        return "dse.command.get";
    }

    @Override
    public String getCommandDesc() {
        return "/dse get 玩家 槽位 是否拿取(true/false)";
    }
}
