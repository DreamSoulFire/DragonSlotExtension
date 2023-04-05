package com.soulflame.dragonslotextension.commands.subcommand;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.commands.CommandBase;
import com.soulflame.dragonslotextension.utils.SlotUtil;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeSlot extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        TextUtil.sendMessage(sender, DragonSlotExtension.message.cantUseInConsole);
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            TextUtil.sendMessage(player, DragonSlotExtension.message.playerOffline);
            return;
        }
        SlotUtil.changeSlot(player, args[1], args[2]);
        TextUtil.sendMessage(player, DragonSlotExtension.message.slotItemChange);
    }

    @Override
    public String getPermission() {
        return "dse.command.change";
    }

    @Override
    public String getCommandDesc() {
        return "/dse change 玩家 槽位1 槽位2";
    }
}