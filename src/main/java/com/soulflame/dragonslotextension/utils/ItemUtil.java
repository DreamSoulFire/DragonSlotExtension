package com.soulflame.dragonslotextension.utils;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.filemanager.config.EquipChance;
import com.soulflame.dragonslotextension.filemanager.config.MappingSlot;
import com.soulflame.dragonslotextension.filemanager.config.Message;
import com.soulflame.dragonslotextension.filemanager.entity.EquipChanceData;
import com.soulflame.dragonslotextension.filemanager.entity.MappingData;
import eos.moe.dragoncore.api.SlotAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemUtil {
    private static final Message message = DragonSlotExtension.message;
    private static final MappingSlot mapping = DragonSlotExtension.mappingSlot;

    /**
     * 用于检测物品中的识别lore
     * @param item 需要进行识别的物品
     * @return 是否通过检测
     */
    public static boolean isTrueItem(ItemStack item) {
        boolean is_true_item = false;
        if (item == null || Material.AIR.equals(item.getType())) return false;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return false;
        List<String> lores = meta.getLore();
        String last_lore = lores.get(lores.size() - 1);
        Map<String, MappingData> dataMap = mapping.map;
        for (String key : dataMap.keySet()) {
            MappingData data = dataMap.get(key);
            List<String> config_lores = data.getLore();
            String last_config_lore = config_lores.get(config_lores.size() - 1);
            last_config_lore = ChatColor.translateAlternateColorCodes('&', last_config_lore);
            if (!last_lore.equalsIgnoreCase(last_config_lore)) {
                is_true_item = false;
                continue;
            }
            is_true_item = true;
        }
        return is_true_item;
    }

    /**
     * 清除玩家的违规物品
     */
    public static void removeItemInPlayer(Player player) {
        for (ItemStack item : player.getInventory()) {
            if (!isTrueItem(item)) continue;
            item.setAmount(0);
            TextUtil.sendMessage(player, DragonSlotExtension.message.haveErrorItem);
        }
    }

    /**
     * 添加识别lore
     * @param itemStack 被添加的物品
     * @param lores 获取的lore
     */
    public static void addCheckLore(ItemStack itemStack, List<String> lores) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;
        if (!meta.hasLore()) return;
        if (isTrueItem(itemStack)) return;
        List<String> line = meta.getLore();
        for (String lore : lores) {
            lore = TextUtil.replaceColor(lore);
            line.add(lore);
        }
        meta.setLore(line);
        itemStack.setItemMeta(meta);
    }

    public static double getLoreDouble(List<String> lore, String regex) {
        double max = 0.0D;
        if (lore.isEmpty() || regex.isEmpty()) return max;
        String s = ChatColor.stripColor(lore.toString());
        Pattern compile = Pattern.compile(regex + ":? *([\\d.]+)");
        Matcher matcher = compile.matcher(s);
        while (matcher.find()) {
            String group = matcher.group(1);
            try {
                max += Double.parseDouble(group);
            } catch (Exception ignored) {}
        }
        return max;
    }

    public static void checkItem(Player player, ItemStack item, ItemStack addItem, String text, String value) {
        if (!text.contains(value) && !text.equalsIgnoreCase(value)) return;
        item.setAmount(item.getAmount() - 1);
        TextUtil.sendMessage(player, message.saveEquipItem);
        player.getInventory().addItem(addItem);
    }

    public static void saveItem(Player player, ItemStack item) {
        boolean isError = false;
        for (String save : DragonSlotExtension.config.saveItems) {
            if (!save.contains("<->")) {
                TextUtil.sendMessage(message.fileError);
                continue;
            }
            String[] split = save.split("<->");
            if (split.length != 2) {
                TextUtil.sendMessage(message.fileError);
                isError = true;
                continue;
            }
            PlayerInventory inventory = player.getInventory();
            if ("permission".equalsIgnoreCase(split[0])) {
                if (!player.hasPermission(split[1])) continue;
                TextUtil.sendMessage(player, message.saveEquipItem);
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

    public static void success(AtomicBoolean isSuccess, Player player, EquipChanceData data, ItemStack item) {
        isSuccess.set(true);
        ItemMeta itemMeta = item.getItemMeta();
        CommandUtil.run(player, data.getCommands(), isSuccess.get());
        String success = message.equipSuccess;
        if (!itemMeta.hasDisplayName()) success = success.replace("<item>", item.getType().name());
        else success = success.replace("<item>", itemMeta.getDisplayName());
        TextUtil.sendMessage(player, success);
    }

    public static void success(AtomicBoolean isSuccess, Player player, EquipChanceData data,
                               String identifier, String viewSlot, ItemStack item) {
        isSuccess.set(true);
        ItemMeta itemMeta = item.getItemMeta();
        CommandUtil.run(player, data.getCommands(), isSuccess.get());
        SlotAPI.setSlotItem(player, identifier, item, true);
        String success = message.equipSuccess;
        if (!itemMeta.hasDisplayName()) success = success.replace("<item>", item.getType().name());
        else success = success.replace("<item>", itemMeta.getDisplayName());
        item.setType(Material.AIR);
        SlotAPI.setSlotItem(player, viewSlot, item, true);
        TextUtil.sendMessage(player, success);
    }

    public static void fail(AtomicBoolean isSuccess, Player player, EquipChanceData data,
                            String viewSlot, ItemStack item) {
        CommandUtil.run(player, data.getCommands(), isSuccess.get());
        saveItem(player, item);
        ItemMeta itemMeta = item.getItemMeta();
        String equipFail = message.equipFail;
        if (!itemMeta.hasDisplayName()) equipFail = equipFail.replace("<item>", item.getType().name());
        else equipFail = equipFail.replace("<item>", itemMeta.getDisplayName());
        item.setType(Material.AIR);
        SlotAPI.setSlotItem(player, viewSlot, item, true);
        TextUtil.sendMessage(player, equipFail);
    }

    public static void lastRun(AtomicBoolean isSuccess, boolean isGui, Player player, EquipChanceData data, double chance, double randomChance,
                               String identifier, String viewSlot, ItemStack item) {
        if (chance / 100.0D >= randomChance) {
            if (!isGui) {
                success(isSuccess, player, data, item);
                return;
            }
            success(isSuccess, player, data, identifier, viewSlot, item);
            return;
        }
        fail(isSuccess, player, data, viewSlot, item);
    }

    public static void runEquip(ItemStack item, String viewSlot, String identifier, Player player, boolean isGui) {
        double randomChance = new Random().nextDouble();
        if (item == null || Material.AIR.equals(item.getType())) return;
        if (viewSlot == null) return;
        EquipChance yaml = DragonSlotExtension.equipChance;
        Map<String, EquipChanceData> dataMap = yaml.map;
        AtomicBoolean isSuccess = new AtomicBoolean(false);
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
                lastRun(isSuccess, isGui, player, data, chance, randomChance, identifier, viewSlot, item);
            });
        }
    }
}
