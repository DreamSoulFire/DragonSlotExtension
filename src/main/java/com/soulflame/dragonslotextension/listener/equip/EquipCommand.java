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
        map.keySet().forEach(k->{
            EquipSlotCmdData data = map.get(k);
            List<String> commands = data.getCommands();
            List<String> slots = data.getSlots();
            slots.forEach(s -> {
                if (!identifier.equalsIgnoreCase(s)) return;
                String check = data.getCheck();
                if (!check.contains("<->")) {
                    TextUtil.sendMessage(DragonSlotExtension.message.fileError);
                    return;
                }
                String[] split = check.split("<->");
                if (split.length != 2) {
                    TextUtil.sendMessage(DragonSlotExtension.message.fileError);
                    return;
                }
                if ("name".equalsIgnoreCase(split[0])) {
                    String displayName = itemMeta.getDisplayName();
                    if (displayName == null) return;
                    if (!displayName.contains(split[1])) return;
                    CommandUtil.run(player, commands);
                }
                if ("lore".equalsIgnoreCase(split[0])) {
                    List<String> lore = itemMeta.getLore();
                    if (lore == null) return;
                    for (String line : lore) {
                     if (!line.contains(split[1]) && !line.equalsIgnoreCase(split[1])) continue;
                     CommandUtil.run(player, commands);
                    }
                }
            });
        });
    }

}
