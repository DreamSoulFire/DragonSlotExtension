package com.soulflame.dragonslotextension.commands.subcommand;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.commands.CommandBase;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class Reload extends CommandBase {

    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        File folder = DragonSlotExtension.getPlugin().getDataFolder();
        DragonSlotExtension.config.reload(folder, "config.yml");
        DragonSlotExtension.equipChance.reload(folder, "modules/equip-chance.yml");
        DragonSlotExtension.equipCommand.reload(folder, "modules/equip-command.yml");
        DragonSlotExtension.equipChanceGui.reload(folder, "gui/equip-chance-gui.yml");
        DragonSlotExtension.mappingSlot.reload(folder, "modules/mapping.yml");
        DragonSlotExtension.message.reload(folder, "message.yml");
        DragonSlotExtension.swapSlot.reload(folder, "modules/swap.yml");
        TextUtil.sendMessage(sender, DragonSlotExtension.message.reload);
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "dse.command.reload";
    }

    @Override
    public String getCommandDesc() {
        return "/dse reload";
    }
}
