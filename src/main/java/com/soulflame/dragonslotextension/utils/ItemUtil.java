package com.soulflame.dragonslotextension.utils;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.filemanager.config.EquipChance;
import com.soulflame.dragonslotextension.filemanager.config.MessageFile;
import com.soulflame.dragonslotextension.filemanager.entity.EquipChanceData;
import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.database.IDataBase;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemUtil {
    private static final MessageFile message = DragonSlotExtension.message;

    /**
     * 获取物品lore上的数值
     * @param lore lore
     * @param regex 检测标识
     * @return 数值
     */
    public static double getLoreDouble(List<String> lore, String regex) {
        double max = 0.0D;
        if (lore.isEmpty() || regex.isEmpty()) return max;
        String s = ChatColor.stripColor(lore.toString());
        Pattern compile = Pattern.compile(regex + ":? *([\\d.]+)");
        Matcher matcher = compile.matcher(s);
        while (matcher.find()) {
            String group = matcher.group(1);
            max += Double.parseDouble(group);
        }
        return max;
    }

    /**
     * 检查物品是否包含文本
     * 包含则为玩家添加物品
     * 且发送物品保留信息
     * @param player 玩家
     * @param item 物品
     * @param addItem 保留的物品
     * @param text 文本
     * @param value 包含的文本
     */
    public static void checkItem(Player player, ItemStack item, ItemStack addItem, String text, String value) {
        if (!text.contains(value) && !text.equalsIgnoreCase(value)) return;
        item.setAmount(item.getAmount() - 1);
        TextUtil.sendMessage(player, message.equipSave);
        player.getInventory().addItem(addItem);
    }

    public static void changeLore(Player player, String slot, List<String> plans) {
        plans = PlaceholderAPI.setPlaceholders(player, plans);
        List<String> finalPlans = plans;
        SlotAPI.getSlotItem(player, slot, new IDataBase.Callback<ItemStack>() {
            @Override
            public void onResult(ItemStack itemStack) {
                if (itemStack == null || Material.AIR.equals(itemStack.getType())) return;
                ItemMeta meta = itemStack.getItemMeta();
                List<String> lores = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                for (String plan : finalPlans) {
                    if (!plan.contains("<->")) {
                        TextUtil.sendMessage(message.fileError);
                        break;
                    }
                    String[] split = plan.split("<->");
                    if (split.length != 2) {
                        TextUtil.sendMessage(message.fileError);
                        break;
                    }
                    for (int i = 0; i < lores.size(); i++) {
                        String lore = lores.get(i);
                        if (!ChatColor.stripColor(lore).equalsIgnoreCase(split[0])) continue;
                        lores.set(i, split[1]);
                    }
                }
                meta.setLore(lores);
                itemStack.setItemMeta(meta);
                SlotAPI.setSlotItem(player, slot, itemStack, true);
            }

            @Override
            public void onFail() {
                TextUtil.sendMessage(player, DragonSlotExtension.message.itemError);
            }
        });
    }

    /**
     * 保留物品
     * @param player 玩家
     * @param item 保留的物品
     */
    public static void saveItem(Player player, ItemStack item) {
        boolean isError = false;
        for (String save : DragonSlotExtension.config.saveItems) {
            if (!save.contains("<->")) {
                TextUtil.sendMessage(message.fileError);
                break;
            }
            String[] split = save.split("<->");
            if (split.length != 2) {
                TextUtil.sendMessage(message.fileError);
                break;
            }
            PlayerInventory inventory = player.getInventory();
            if ("permission".equalsIgnoreCase(split[0])) {
                if (!player.hasPermission(split[1])) continue;
                TextUtil.sendMessage(player, message.equipSave);
                inventory.addItem(item);
                break;
            }
            for (ItemStack _item : inventory) {
                if (_item == null || Material.AIR.equals(_item.getType())) continue;
                ItemMeta _meta = _item.getItemMeta();
                if ("name".equalsIgnoreCase(split[0])) {
                    if (!_meta.hasDisplayName()) continue;
                    String name = _meta.getDisplayName();
                    name = ChatColor.stripColor(name);
                    checkItem(player, _item, item, name, split[1]);
                    break;
                }
                if (!"lore".equalsIgnoreCase(split[0])) continue;
                for (String line : _meta.getLore()) {
                    line = ChatColor.stripColor(line);
                    checkItem(player, _item, item, line, split[1]);
                    break;
                }
                isError = true;
            }
            if (isError) TextUtil.sendMessage(message.fileError);
        }
    }

    /**
     * 界面装备物品时
     * 成功装备物品执行
     * @param player 玩家
     * @param data 节点数据
     * @param identifier 槽位
     * @param viewSlot 过度槽位
     * @param item 物品
     */
    public static void success(Player player, EquipChanceData data,
                               String identifier, String viewSlot, ItemStack item, String check) {
        String success = getString(player, data, item);
        if ("gui".equalsIgnoreCase(check)) {
            SlotAPI.setSlotItem(player, identifier, item, true);
            item.setType(Material.AIR);
            SlotAPI.setSlotItem(player, viewSlot, item, true);
            TextUtil.sendMessage(player, success);
        } else if ("click".equalsIgnoreCase(check)) {
            TextUtil.sendMessage(player, success);
        }
    }

    @NotNull
    private static String getString(Player player, EquipChanceData data, ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        String success = message.equipSuccess;
        String itemName = itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : item.getType().name();
        CommandUtil.run(player, data.getCommands(), itemName, "success");
        success = success.replace("<item>", itemName);
        return success;
    }

    /**
     * 物品装备失败执行
     * @param player 玩家
     * @param data 节点数据
     * @param viewSlot 过度槽位
     * @param item 物品
     */
    public static void fail(Player player, EquipChanceData data,
                            String viewSlot, ItemStack item) {
        saveItem(player, item);
        ItemMeta itemMeta = item.getItemMeta();
        String equipFail = message.equipFail;
        String itemName = itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : item.getType().name();
        CommandUtil.run(player, data.getCommands(), itemName, "fail");
        equipFail = equipFail.replace("<item>", itemName);
        item.setType(Material.AIR);
        SlotAPI.setSlotItem(player, viewSlot, item, true);
        TextUtil.sendMessage(player, equipFail);
    }

    /**
     * 检测几率
     * @param check 是否是gui
     * @param player 玩家
     * @param data 节点数据
     * @param chance 几率
     * @param randomChance 随机几率
     * @param identifier 槽位
     * @param viewSlot 过度槽位
     * @param item 物品
     */
    public static void lastRun(String check, Player player, EquipChanceData data, double chance, double randomChance,
                               String identifier, String viewSlot, ItemStack item) {
        if (chance / 100.0D >= randomChance) {
            success(player, data, identifier, viewSlot, item, check);
            return;
        }
        fail(player, data, viewSlot, item);
    }

    /**
     * 装备物品
     * @param item 物品
     * @param viewSlot 过度槽位
     * @param identifier 槽位
     * @param player 玩家
     * @param check 是否是gui
     */
    public static void runEquip(ItemStack item, String viewSlot, String identifier, Player player, String check) {
        double randomChance = new Random().nextDouble();
        if (item == null || Material.AIR.equals(item.getType())) return;
        if (viewSlot == null) return;
        EquipChance yaml = DragonSlotExtension.equipChance;
        Map<String, EquipChanceData> dataMap = yaml.map;
        for (String key : dataMap.keySet()) {
            EquipChanceData data = dataMap.get(key);
            List<String> dragonSlot = data.getSlotList();
            dragonSlot.forEach(slot -> {
                if (!identifier.equalsIgnoreCase(slot)) return;
                ItemMeta itemMeta = item.getItemMeta();
                if (itemMeta == null || !itemMeta.hasLore()) return;
                List<String> lore = itemMeta.getLore();
                double chance = ItemUtil.getLoreDouble(lore, data.getIdentifier());
                if (DragonSlotExtension.config.debugEquipChance) {
                    for (String line : message.debugEquipChance) {
                        line = line.replace("<random>", String.valueOf(randomChance));
                        line = line.replace("<chance>", String.valueOf(chance / 100));
                        TextUtil.sendMessage(line);
                    }
                }
                lastRun(check, player, data, chance, randomChance, identifier, viewSlot, item);
            });
        }
    }
}
