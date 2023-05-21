package com.soulflame.dragonslotextension.utils;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.database.IDataBase;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SlotUtil {
    /**
     * 获取玩家槽位上的物品
     * @param player 玩家
     * @param target 需要获取槽位的玩家
     * @param slot 龙核槽位
     * @param isTake 是否拿取该槽位的物品
     */
    public static void getSlot(final Player player, Player target, String slot, boolean isTake) {
        final List<ItemStack> items = new ArrayList<>();
        SlotAPI.getSlotItem(target, slot, new IDataBase.Callback<ItemStack>() {
            public void onResult(ItemStack itemStack) {
                if (itemStack == null || Material.AIR.equals(itemStack.getType())) {
                    TextUtil.sendMessage(player, DragonSlotExtension.message.slotItemAir);
                    return;
                }
                items.add(itemStack);
                player.getInventory().addItem(itemStack);
            }

            public void onFail() {
                TextUtil.sendMessage(player, DragonSlotExtension.message.itemError);
            }
        });
        if (isTake) SlotAPI.setSlotItem(target, slot, new ItemStack(Material.AIR), true);
        ItemStack itemStack = items.get(0);
        ItemMeta meta = itemStack.getItemMeta();
        String name = meta.hasDisplayName() ? meta.getDisplayName() : itemStack.getType().name();
        int amount = itemStack.getAmount();
        String slotItemGet = DragonSlotExtension.message.slotItemGet
                .replace("<target>", target.getName())
                .replace("<item>", name)
                .replace("<amount>", String.valueOf(amount))
                .replace("<slot>", slot);
        TextUtil.sendMessage(player, slotItemGet);
    }

    /**
     * 设置玩家槽位上的物品为手持物品
     * @param player 玩家
     * @param target 需要设置的玩家
     * @param slot 龙核槽位
     * @param isForce 是否强制设置
     * @param isTake 是否拿取手上物品
     */
    public static void setSlot(Player player, Player target, String slot, boolean isForce, boolean isTake) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || Material.AIR.equals(itemStack.getType())) {
            TextUtil.sendMessage(player, DragonSlotExtension.message.itemInHandAir);
            return;
        }
        String slotItemSet = DragonSlotExtension.message.slotItemSet.replace("<slot>", slot);
        final boolean[] play = {true};
        if (isForce) {
            SlotAPI.setSlotItem(target, slot, itemStack, true);
            TextUtil.sendMessage(player, slotItemSet);
        } else {
            SlotAPI.getSlotItem(target, slot, new IDataBase.Callback<ItemStack>() {
                @Override
                public void onResult(ItemStack item) {
                    if (item != null && !Material.AIR.equals(item.getType())) {
                        TextUtil.sendMessage(player, DragonSlotExtension.message.slotItemNotAir);
                        play[0] = false;
                        return;
                    }
                    TextUtil.sendMessage(player, slotItemSet);
                    SlotAPI.setSlotItem(target, slot, itemStack, true);
                }

                @Override
                public void onFail() {
                    TextUtil.sendMessage(player, DragonSlotExtension.message.itemError);
                }
            });
        }
        if (!isTake || !play[0]) return;
        itemStack.setAmount(0);
    }

    /**
     * 为玩家交换物品
     * @param player 玩家
     * @param preSlot 第一个槽位
     * @param newSlot 第二个槽位
     */
    public static void changeSlot(Player player, String preSlot, String newSlot) {
        SlotAPI.getSlotItem(player, preSlot, new IDataBase.Callback<ItemStack>() {
            public void onResult(ItemStack item) {
                SlotAPI.getSlotItem(player, newSlot, new IDataBase.Callback<ItemStack>() {
                    public void onResult(ItemStack itemStack) {
                        SlotAPI.setSlotItem(player, preSlot, itemStack, true);
                    }

                    public void onFail() {
                        TextUtil.sendMessage(player, DragonSlotExtension.message.itemError);
                    }
                });
                SlotAPI.setSlotItem(player, newSlot, item, true);
            }

            public void onFail() {
                TextUtil.sendMessage(player, DragonSlotExtension.message.itemError);
            }
        });
    }

    /**
     * 移除玩家槽位上的物品
     * @param sender 发送信息的玩家
     * @param target 移除槽位的玩家
     * @param slot 龙核槽位
     */
    public static void removeSlot(CommandSender sender, Player target, String slot) {
        SlotAPI.getSlotItem(target, slot, new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack item) {
                if (item == null || Material.AIR.equals(item.getType())) {
                    TextUtil.sendMessage(sender, DragonSlotExtension.message.slotItemAir);
                    return;
                }
                String slotItemRemove = DragonSlotExtension.message.slotItemRemove.replace("<slot>", slot);
                ItemMeta meta = item.getItemMeta();
                String name = meta.hasDisplayName() ? meta.getDisplayName() : item.getType().name();
                slotItemRemove = slotItemRemove.replace("<item>", name);
                SlotAPI.setSlotItem(target, slot, new ItemStack(Material.AIR), true);
                TextUtil.sendMessage(sender, slotItemRemove);
            }

            @Override
            public void onFail() {
                TextUtil.sendMessage(sender, DragonSlotExtension.message.itemError);
            }
        });
    }
}
