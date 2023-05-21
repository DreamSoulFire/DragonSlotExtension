package com.soulflame.dragonslotextension.commands.subcommand;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.commands.CommandBase;
import com.soulflame.dragonslotextension.filemanager.entity.ChangeLoreData;
import com.soulflame.dragonslotextension.utils.ItemUtil;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class ChangeLore extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            TextUtil.sendMessage(sender, DragonSlotExtension.message.playerOffline);
            return;
        }
        Map<String, ChangeLoreData> map = DragonSlotExtension.changeLore.map;
        ChangeLoreData data = map.get(args[1]);
        if (data == null) {
            TextUtil.sendMessage(sender, DragonSlotExtension.message.changeLoreFail);
            return;
        }
        TextUtil.sendMessage(sender, DragonSlotExtension.message.changeLoreSuccess.replace("<plan>", args[1]));
        ItemUtil.changeLore(player, data.getSlot(), data.getPlans());
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {

    }

    @Override
    public String getPermission() {
        return "dse.command.change.lore";
    }

    @Override
    public String getCommandDesc() {
        return "/dse change-lore 玩家 计划";
    }
}
