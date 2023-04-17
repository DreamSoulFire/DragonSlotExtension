package com.soulflame.dragonslotextension.commands.subcommand;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.commands.CommandBase;
import com.soulflame.dragonslotextension.filemanager.config.MessageFile;
import com.soulflame.dragonslotextension.utils.TextUtil;
import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.database.IDataBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Multi extends CommandBase {
    @Override
    public void onConsoleCommand(CommandSender sender, String[] args) {
        MessageFile message = DragonSlotExtension.message;
        YamlConfiguration yaml = message.getYaml();
        ConfigurationSection section = yaml.getConfigurationSection("multi");
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            TextUtil.sendMessage(sender, message.playerOffline);
            return;
        }
        SlotAPI.getSlotItem(player, args[1], new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack itemStack) {
                if (itemStack == null || Material.AIR.equals(itemStack.getType())) {
                    TextUtil.sendMessage(sender, message.slotItemAir);
                    return;
                }
                int amount = itemStack.getAmount();
                amount = amount * Integer.parseInt(args[2]);
                if (amount > 127) {
                    TextUtil.sendMessage(player, section.getString("over", "&c物品数量大于127, 无法翻倍"));
                    return;
                }
                Random random = new Random();
                int ranChance = random.nextInt(100);
                if (ranChance > Integer.parseInt(args[3])) {
                    TextUtil.sendMessage(player, section.getString("fail", "&c物品翻倍失败"));
                    if (!Boolean.parseBoolean(args[4])) return;
                    SlotAPI.setSlotItem(player, args[1], new ItemStack(Material.AIR), true);
                    return;
                }
                TextUtil.sendMessage(player, section.getString("success", "&a物品翻倍成功"));
                itemStack.setAmount(amount);
                SlotAPI.setSlotItem(player, args[1], itemStack, true);
            }

            @Override
            public void onFail() {
                TextUtil.sendMessage(sender, message.itemError);
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
        return "/dse multi 玩家 槽位 倍数 概率(整数,1就是百分之一的概率) 失败是否清除(true/false)";
    }
}
