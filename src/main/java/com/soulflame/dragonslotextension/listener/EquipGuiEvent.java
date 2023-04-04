package com.soulflame.dragonslotextension.listener;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.filemanager.config.ConfigFile;
import com.soulflame.dragonslotextension.filemanager.entity.EquipChanceData;
import com.soulflame.dragonslotextension.utils.ItemUtil;
import com.soulflame.dragonslotextension.utils.TextUtil;
import eos.moe.dragoncore.api.SlotAPI;
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent;
import eos.moe.dragoncore.config.Config;
import eos.moe.dragoncore.config.SlotSetting;
import eos.moe.dragoncore.database.IDataBase;
import eos.moe.dragoncore.network.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipGuiEvent implements Listener {

    private static final Map<String, String> map = new HashMap<>();
    private static final ConfigFile configFile = DragonSlotExtension.config;
    private static String clickSlot;
    private static String viewSlot;
    private static final List<Player> allowTake = new ArrayList<>();

    @EventHandler
    public void debug(CustomPacketEvent event) {
        System.out.println(event.getIdentifier());
        System.out.println(event.getData());
    }

    @EventHandler
    public void openScreen(CustomPacketEvent event) {
        List<String> data = event.getData();
        Player player = event.getPlayer();
        if (!"screen_open".equalsIgnoreCase(data.get(0))) return;
        if (!"gui/equip-chance-gui.yml".equalsIgnoreCase(data.get(1))) return;
        map.put("dse_equip_view", "");
        PacketSender.sendSyncPlaceholder(player, map);
    }

    @EventHandler
    public void clickConfirm(CustomPacketEvent event) {
        if (!"DragonCore_ClickSlot".equalsIgnoreCase(event.getIdentifier())) return;
        Player player = event.getPlayer();
        ItemStack cursor = player.getItemOnCursor();
        List<String> eventData = event.getData();
        Map<String, EquipChanceData> dataMap = DragonSlotExtension.equipChance.equipChanceMap;
        dataMap.keySet().forEach(key -> {
            EquipChanceData data = dataMap.get(key);
            data.getSlotList().forEach(slot -> {
                if (!slot.equalsIgnoreCase(eventData.get(0))) return;
                if (cursor != null && !Material.AIR.equals(cursor.getType())) return;
                if (!allowTake.contains(player)) {
                    allowTake.add(player);
                    event.setCancelled(true);
                    String notice = DragonSlotExtension.message.takeNotice;
                    notice = notice.replace("<time>", String.valueOf(configFile.equipTakeTime / 20));
                    TextUtil.sendMessage(player, notice);
                }
                Bukkit.getScheduler().runTaskLaterAsynchronously(DragonSlotExtension.getPlugin(),
                        () -> allowTake.remove(player), configFile.equipTakeTime);
            });
        });
    }

    @EventHandler
    public void onClickSlot(CustomPacketEvent event) {
        if (!"gui".equalsIgnoreCase(configFile.equipMode)) return;
        if (!"DragonCore_ClickSlot".equalsIgnoreCase(event.getIdentifier())) return;
        Player player = event.getPlayer();
        List<String> eventData = event.getData();
        Map<String, EquipChanceData> dataMap = DragonSlotExtension.equipChance.equipChanceMap;
        dataMap.keySet().forEach(key -> {
            EquipChanceData data = dataMap.get(key);
            data.getSlotList().forEach(slot -> {
                map.put("dse_equip_view_slot", configFile.equipViewSlot);
                if (!slot.equalsIgnoreCase(eventData.get(0))) {
                    if (!eventData.get(0).equalsIgnoreCase(configFile.equipViewSlot)) return;
                    viewSlot = eventData.get(0);
                    return;
                }
                clickSlot = eventData.get(0);
                SlotAPI.getSlotItem(player, clickSlot, new IDataBase.Callback<ItemStack>() {
                    @Override
                    public void onResult(ItemStack item) {
                        if (item != null && !Material.AIR.equals(item.getType())) return;
                        event.setCancelled(true);
                        map.replace("dse_equip_view", "true");
                        PacketSender.sendSyncPlaceholder(player, map);
                    }

                    @Override
                    public void onFail() {
                        TextUtil.sendMessage(DragonSlotExtension.message.itemError);
                    }
                });
            });
        });
        String view = configFile.equipViewSlot;
        ConfigurationSection section = configFile.getYaml().getConfigurationSection("global-setting.equip-chance");
        Config.slotSettings.put(view, new SlotSetting(section));
    }

    @EventHandler
    public void onEquip(CustomPacketEvent event) {
        Player player = event.getPlayer();
        String identifier = event.getIdentifier();
        List<String> data = event.getData();
        if (!"dse".equalsIgnoreCase(identifier)) return;
        if ("equip".equalsIgnoreCase(data.get(0))) {
            SlotAPI.getSlotItem(player, data.get(1), new IDataBase.Callback<ItemStack>() {
                @Override
                public void onResult(ItemStack itemStack) {
                    if (itemStack == null || Material.AIR.equals(itemStack.getType())) {
                        TextUtil.sendMessage(player, DragonSlotExtension.message.equipNone);
                        return;
                    }
                    ItemUtil.runEquip(itemStack, viewSlot, clickSlot, player, true);
                }
                @Override
                public void onFail() {
                    TextUtil.sendMessage(DragonSlotExtension.message.itemError);
                }
            });
        }
        if ("view".equalsIgnoreCase(data.get(0))) {
            map.replace("dse_equip_view", data.get(1));
            PacketSender.sendSyncPlaceholder(player, map);
        }
    }
}
