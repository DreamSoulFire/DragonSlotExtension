package com.soulflame.dragonslotextension.commands.subcommand;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.commands.CommandBase;
import com.soulflame.dragonslotextension.utils.TextUtil;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        Player player = args[0].equals("me") ? (Player) sender : Bukkit.getPlayer(args[0]);
        if (player == null) {
            TextUtil.sendMessage(sender, DragonSlotExtension.message.playerOffline);
            return;
        }
        PacketSender.sendYaml(player, "gui/equip-chance-gui.yml", DragonSlotExtension.equipChanceGui.getYaml());
        player.openInventory(Bukkit.createInventory(null, 0, DragonSlotExtension.equipChanceGui.match));
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "dse.command.open";
    }

    @Override
    public String getCommandDesc() {
        return "/dse open 玩家";
    }
}
