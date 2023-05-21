package com.soulflame.dragonslotextension.listener.equip;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.filemanager.entity.EquipSlotCmdData;
import com.soulflame.dragonslotextension.utils.CommandUtil;
import com.soulflame.dragonslotextension.utils.TextUtil;
import eos.moe.dragoncore.api.event.PlayerSlotUpdateEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class EquipCommand implements Listener {

    @EventHandler
    public void onEquip(PlayerSlotUpdateEvent event) {
        Player player = event.getPlayer();
        String identifier = event.getIdentifier();
        ItemStack itemStack = event.getItemStack();
        if (itemStack == null || Material.AIR.equals(itemStack.getType())) return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        Map<String, EquipSlotCmdData> map = DragonSlotExtension.equipCommand.map;
        for (String key : map.keySet()) {
            EquipSlotCmdData data = map.get(key);
            List<String> commands = data.getCommands();
            List<String> check = data.getCheck();
            if (!check(identifier, itemMeta, check)) continue;
            CommandUtil.run(player, commands, itemStack.getAmount(), itemMeta);
        }
    }

    private static boolean check(String identifier, ItemMeta itemMeta, List<String> check) {
        boolean isMatch = false;
        for (String list : check) {
            if (!list.contains("<->")) {
                TextUtil.sendMessage(DragonSlotExtension.message.fileError);
                isMatch = false;
                continue;
            }
            String[] split = list.split("<->");
            if (split.length != 3) {
                TextUtil.sendMessage(DragonSlotExtension.message.fileError);
                isMatch = false;
                continue;
            }
            if (!identifier.equalsIgnoreCase(split[0])) {
                isMatch = false;
                continue;
            }
            if ("name".equalsIgnoreCase(split[1])) {
                String displayName = itemMeta.getDisplayName();
                if (displayName == null) {
                    isMatch = false;
                    continue;
                }
                if (!displayName.contains(split[2]) && !displayName.equalsIgnoreCase(split[2])) {
                    isMatch = false;
                    continue;
                }
                isMatch = true;
            }
            if ("lore".equalsIgnoreCase(split[1])) {
                List<String> lore = itemMeta.getLore();
                if (lore == null) {
                    isMatch = false;
                    continue;
                }
                for (String line : lore) {
                    if (!line.contains(split[2]) && !line.equalsIgnoreCase(split[2])) {
                        isMatch = false;
                        continue;
                    }
                    isMatch = true;
                }
            }
        }
        return isMatch;
    }

}
