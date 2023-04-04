package com.soulflame.dragonslotextension.listener;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.utils.ItemUtil;
import eos.moe.dragoncore.api.event.PlayerSlotUpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class EquipChanceEvent implements Listener {

    @EventHandler
    public void onEquip(PlayerSlotUpdateEvent event) {
        if (!"normal".equalsIgnoreCase(DragonSlotExtension.config.equipMode)) return;
        Player player = event.getPlayer();
        String identifier = event.getIdentifier();
        ItemStack item = event.getItemStack();
        ItemUtil.runEquip(item, identifier, identifier, player, false);
    }
}
