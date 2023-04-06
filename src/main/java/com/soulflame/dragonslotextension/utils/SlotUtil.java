package com.soulflame.dragonslotextension.utils;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.database.IDataBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SlotUtil {
    public static void getSlot(final Player player, Player target, String slot, boolean isTake) {
        final ItemStack[] item = new ItemStack[1];
        SlotAPI.getSlotItem(target, slot, new IDataBase.Callback<ItemStack>() {
            public void onResult(ItemStack itemStack) {
                if (itemStack == null || Material.AIR.equals(itemStack.getType())) {
                    TextUtil.sendMessage(player, DragonSlotExtension.message.slotItemAir);
                    return;
                }
                item[0] = itemStack;
                player.getInventory().addItem(itemStack);
            }

            public void onFail() {
                TextUtil.sendMessage(player, DragonSlotExtension.message.itemError);
            }
        });
        if (isTake) SlotAPI.setSlotItem(target, slot, new ItemStack(Material.AIR), true);
        ItemMeta meta = item[0].getItemMeta();
        if (!meta.hasDisplayName()) return;
        String name = meta.getDisplayName();
        int amount = item[0].getAmount();
        String slotItemGet = DragonSlotExtension.message.slotItemGet;
        slotItemGet = slotItemGet.replace("target", target.getName());
        slotItemGet = slotItemGet.replace("<item>", name);
        slotItemGet = slotItemGet.replace("amount", String.valueOf(amount));
        slotItemGet = slotItemGet.replace("slot", slot);
        TextUtil.sendMessage(player, slotItemGet);
    }

    public static void setSlot(Player player, Player target, String slot, boolean isForce, boolean isTake) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || Material.AIR.equals(itemStack.getType())) {
            TextUtil.sendMessage(player, DragonSlotExtension.message.itemInHandAir);
            return;
        }
        String slotItemSet = DragonSlotExtension.message.slotItemSet;
        slotItemSet = slotItemSet.replace("<slot>", slot);
        if (isForce) {
            SlotAPI.setSlotItem(target, slot, itemStack, true);
            TextUtil.sendMessage(player, slotItemSet);
            return;
        }
        String finalSlotItemSet = slotItemSet;
        SlotAPI.getSlotItem(target, slot, new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack item) {
                if (item != null && !Material.AIR.equals(item.getType())) {
                    TextUtil.sendMessage(player, DragonSlotExtension.message.slotItemNotAir);
                    return;
                }
                TextUtil.sendMessage(player, finalSlotItemSet);
                SlotAPI.setSlotItem(target, slot, itemStack, true);
            }

            @Override
            public void onFail() {
                TextUtil.sendMessage(player, DragonSlotExtension.message.itemError);
            }
        });
        if (!isTake) return;
        itemStack.setAmount(itemStack.getAmount() - 1);
    }

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

    public static void removeSlot(Player player, Player target, String slot) {
        SlotAPI.getSlotItem(target, slot, new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack item) {
                if (item == null || Material.AIR.equals(item.getType())) {
                    TextUtil.sendMessage(player, DragonSlotExtension.message.slotItemAir);
                    return;
                }
                String slotItemRemove = DragonSlotExtension.message.slotItemRemove;
                slotItemRemove = slotItemRemove.replace("<slot>", slot);
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName())
                    slotItemRemove = slotItemRemove.replace("<item>", meta.getDisplayName());
                else
                    slotItemRemove = slotItemRemove.replace("<item>", meta.getLocalizedName());
                item.setType(Material.AIR);
                SlotAPI.setSlotItem(target, slot, item, true);
                TextUtil.sendMessage(player, slotItemRemove);
            }

            @Override
            public void onFail() {
                TextUtil.sendMessage(player, DragonSlotExtension.message.itemError);
            }
        });
    }
}
