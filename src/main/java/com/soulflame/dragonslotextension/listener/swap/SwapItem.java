package com.soulflame.dragonslotextension.listener.swap;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.filemanager.entity.SwapData;
import com.soulflame.dragonslotextension.utils.SlotUtil;
import com.soulflame.dragonslotextension.utils.TextUtil;
import eos.moe.dragoncore.api.event.KeyPressEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Set;

public class SwapItem implements Listener {

    @EventHandler
    public void onKeyPress(KeyPressEvent event) {
        String key = event.getKey();
        Player player = event.getPlayer();
        if (DragonSlotExtension.config.debugSwapItem) TextUtil.sendMessage(DragonSlotExtension.message.debugSwapItem);
        Map<String, SwapData> map = DragonSlotExtension.swapSlot.map;
        String swapItem = DragonSlotExtension.message.swapItem;
        Set<String> keys = map.keySet();
        boolean isKey = false;
        for (String k : keys) {
            SwapData data = map.get(k);
            String changeKey = data.getKey();
            if (!key.equalsIgnoreCase(changeKey)) continue;
            isKey = true;
            swapItem = swapItem.replace("<plan>", k);
            data.getSlots().forEach(slot -> {
                if (!slot.contains("<->")) {
                    TextUtil.sendMessage(DragonSlotExtension.message.fileError);
                    return;
                }
                String[] split = slot.split("<->");
                SlotUtil.changeSlot(player, split[0], split[1]);
            });
        }
        if (!isKey) return;
        TextUtil.sendMessage(player, swapItem);
    }

}
