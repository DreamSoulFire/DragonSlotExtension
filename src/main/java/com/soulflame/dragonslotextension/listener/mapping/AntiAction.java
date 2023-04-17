package com.soulflame.dragonslotextension.listener.mapping;

import com.soulflame.dragonslotextension.DragonSlotExtension;
import com.soulflame.dragonslotextension.filemanager.config.MessageFile;
import com.soulflame.dragonslotextension.utils.ItemUtil;
import com.soulflame.dragonslotextension.utils.TextUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class AntiAction implements Listener {
    private final MessageFile message = DragonSlotExtension.message;

    /**
     * 玩家丢弃物品事件
     * @param event 事件变量
     */
    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) return;
        Item item_drop = event.getItemDrop();
        ItemStack item = item_drop.getItemStack();
        if (!ItemUtil.isTrueItem(item)) return;
        event.setCancelled(true);
        TextUtil.sendMessage(player, message.cantDrop);
    }

    /**
     * 切换副手事件
     * @param event 事件变量
     */
    @EventHandler
    public void changeHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack item_main = event.getMainHandItem();
        ItemStack item_off = event.getOffHandItem();
        if (ItemUtil.isTrueItem(item_main)) {
            event.setCancelled(true);
            TextUtil.sendMessage(player, message.cantSwapHand);
        }
        if (ItemUtil.isTrueItem(item_off)) {
            event.setCancelled(true);
            TextUtil.sendMessage(player, message.cantSwapHand);
        }
    }

    /**
     * 背包点击事件
     * @param event 事件变量
     */
    @EventHandler
    public void clickInv(InventoryClickEvent event) {
        HumanEntity whoClicked = event.getWhoClicked();
        if (!(whoClicked instanceof Player)) return;
        Player player = ((Player) whoClicked).getPlayer();
        int click_slot = event.getSlot();
        if (DragonSlotExtension.config.debugMapping)
            TextUtil.sendMessage(TextUtil.replaceId(message.debugMapping, click_slot));
        if (player.isOp()) return;
        InventoryView view = event.getView();
        Inventory topInventory = view.getTopInventory();
        if (!InventoryType.CRAFTING.equals(topInventory.getType())) return;
        if (click_slot < 0) return;
        PlayerInventory inventory = player.getInventory();
        if (ClickType.NUMBER_KEY.equals(event.getClick())) {
            if (!ItemUtil.isTrueItem(event.getCurrentItem()) && !ItemUtil.isTrueItem(inventory.getItem(event.getHotbarButton()))) return;
            event.setCancelled(true);
            TextUtil.sendMessage(player, message.cantFastMove);
            return;
        }
        ItemStack item = inventory.getItem(click_slot);
        if (!ItemUtil.isTrueItem(item)) return;
        event.setCancelled(true);
        TextUtil.sendMessage(player, message.cantTake);
    }

    /**
     * 切换物品栏事件
     * @param event 事件变量
     */
    @EventHandler
    public void onSwapItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) return;
        int next = event.getNewSlot();
        ItemStack item = player.getInventory().getItem(next);
        if (!ItemUtil.isTrueItem(item)) return;
        event.setCancelled(true);
        TextUtil.sendMessage(player, message.cantSwap);
    }
}
