package com.soulflame.dragonslotextension.listener.mapping;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.filemanager.config.Message;
import com.soulflame.dragonslotextension.filemanager.entity.MappingData;
import com.soulflame.dragonslotextension.utils.ItemUtil;
import com.soulflame.dragonslotextension.utils.TextUtil;
import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.api.event.PlayerSlotUpdateEvent;
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent;
import eos.moe.dragoncore.database.IDataBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class EquipItem implements Listener {
    private final Message message = DragonSlotExtension.message;
    @EventHandler
    public void clickSlot(CustomPacketEvent event) {
        Player player = event.getPlayer();
        if (!event.getIdentifier().equals("DragonCore_ClickSlot")) return;
        if (event.getData().size() != 2) return;
        String identifier = event.getData().get(0);
        Map<String, MappingData> dataMap = DragonSlotExtension.mappingSlot.map;
        for (String key : dataMap.keySet()) {
            MappingData data = dataMap.get(key);
            String slot = data.getDragonSlots();
            String[] split = slot.split("<->");
            if (split.length < 2) return;
            for (int i = 1; i < split.length; i++) {
                if (!split[i].equalsIgnoreCase(identifier)) continue;
                event.setCancelled(true);
                TextUtil.sendMessage(player, message.cantTake);
            }
        }
    }

    @EventHandler
    public void equipSlot(PlayerSlotUpdateEvent event) {
        Player player = event.getPlayer();
        String dragon_slot = event.getIdentifier();
        if (dragon_slot == null) return;
        Map<String, MappingData> dataMap = DragonSlotExtension.mappingSlot.map;
        for (String key : dataMap.keySet()) {
            MappingData data = dataMap.get(key);
            String slot = data.getDragonSlots();
            List<String> lores = data.getLore();
            String[] split = slot.split("<->");
            if (!dragon_slot.equalsIgnoreCase(split[0])) continue;
            SlotAPI.getSlotItem(player, split[0], new IDataBase.Callback<ItemStack>() {
                @Override
                public void onResult(ItemStack itemStack) {
                    String vanillaSlot = data.getVanillaSlots();
                    if ("".equalsIgnoreCase(vanillaSlot)) return;
                    int vanilla = Integer.parseInt(vanillaSlot);
                    if (itemStack == null || Material.AIR.equals(itemStack.getType())) {
                        player.getInventory().setItem(vanilla, null);
                        if (split.length == 2)
                            SlotAPI.setSlotItem(player, split[1], new ItemStack(Material.AIR, 0), true);
                        return;
                    }
                    ItemUtil.addCheckLore(itemStack, lores);
                    player.getInventory().setItem(vanilla, itemStack);
                    if (split.length != 2) return;
                    if (!ItemUtil.isTrueItem(itemStack)) ItemUtil.addCheckLore(itemStack, lores);
                    SlotAPI.setSlotItem(player, split[1], itemStack, true);
                }
                @Override
                public void onFail() {
                    TextUtil.sendMessage(message.itemError);
                }
            });
        }
    }
}
