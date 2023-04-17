package com.soulflame.dragonslotextension.commands.subcommand;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.commands.CommandBase;
import com.soulflame.dragonslotextension.utils.TextUtil;
import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.database.IDataBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Multi extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            TextUtil.sendMessage(sender, DragonSlotExtension.message.playerOffline);
            return;
        }
        SlotAPI.getSlotItem(player, args[1], new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack itemStack) {
                if (itemStack == null || Material.AIR.equals(itemStack.getType())) {
                    TextUtil.sendMessage(sender, DragonSlotExtension.message.slotItemAir);
                    return;
                }
                Random random = new Random();
                int ranChance = random.nextInt(100);
                int chance = Integer.parseInt(args[3]);
                if (ranChance > chance) {
                    if (!Boolean.parseBoolean(args[4])) return;
                    SlotAPI.setSlotItem(player, args[1], new ItemStack(Material.AIR), true);
                    return;
                }
                int amount = itemStack.getAmount();
                amount = amount * Integer.parseInt(args[2]);
                itemStack.setAmount(amount);
                SlotAPI.setSlotItem(player, args[1], itemStack, true);
            }

            @Override
            public void onFail() {
                TextUtil.sendMessage(sender, DragonSlotExtension.message.itemError);
            }
        });
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        onConsoleCommand(player, args);
    }

    @Override
    public String getPermission() {
        return "dse.command.multi";
    }

    @Override
    public String getCommandDesc() {
        return "/dse multi 玩家 槽位 倍数 概率(整数, 1就是百分之一的概率) 失败是否清除(true/false)";
    }
}
