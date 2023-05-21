package com.soulflame.dragonslotextension.filemanager.config;

import com.soulflame.dragonslotextension.filemanager.FileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class MessageFile extends FileManager {

    public String prefix;
    public List<String> help;
    public String cantUseInConsole;
    public String itemError;
    public String equipSuccess;
    public String equipFail;
    public String equipNone;
    public String takeNotice;
    public String equipSave;
    public String playerOffline;
    public String slotItemAir;
    public String slotItemNotAir;
    public String slotItemGet;
    public String slotItemSet;
    public String slotItemChange;
    public String slotItemRemove;
    public String changeLoreSuccess;
    public String changeLoreFail;
    public String swapItem;
    public String itemInHandAir;
    public String fileError;
    public List<String> debugEquipChance;
    public String debugSwapItem;
    public String reload;

    /**
     * 读取配置文件
     *
     * @param folder 配置文件夹
     * @param name   配置文件名
     */
    public MessageFile(File folder, String name) {
        super(folder, name);
    }

    @Override
    protected void loadData() {
        YamlConfiguration yaml = getYaml();
        prefix = yaml.getString("prefix", "&7[&6Dragon&bSlot&eExtension&7] ");
        swapItem = yaml.getString("swap-item", "&a你成功使用了交换方案&f: &b<plan>");
        debugEquipChance = yaml.getStringList("debug-format.equip-chance");
        debugSwapItem = yaml.getString("debug-format.swap-item", "&6你按下了按键&f: &b<key>");

        ConfigurationSection section = yaml.getConfigurationSection("command");
        help = section.getStringList("help");
        cantUseInConsole = section.getString("cant-use-in-console", "&c后台无法执行该指令");
        playerOffline = section.getString("player-not-online", "&c该玩家不在线, 无法执行指令");
        fileError = section.getString("file-error", "&c配置出错, 请排查错误");
        reload = section.getString("reload", "&a插件重载成功");

        section = yaml.getConfigurationSection("item");
        itemError = section.getString("get-error", "&4物品获取失败, 请检查配置是否出错");
        equipSuccess = section.getString("equip.success", "&a你成功装备了 <item>");
        equipFail = section.getString("equip.fail", "&c装备失败, 物品 <item> 已被销毁");
        equipSave = section.getString("equip.save", "&a已保留装备, 保护符已扣除");
        equipNone = section.getString("equip.none", "&c请放入要装备的物品");
        takeNotice = section.getString("take", "&c请在 <time> 秒内, 连续点击两次以拆卸物品");
        slotItemAir = section.getString("air", "&c该槽位物品为空");
        slotItemNotAir = section.getString("not-air", "&c该槽位物品不为空");
        itemInHandAir = section.getString("in-hand-air", "&c你手上没有任何物品");

        section = yaml.getConfigurationSection("slot");
        slotItemGet = section.getString("get", "&a你得到了玩家 <target> 的 <slot> 里的物品 <item> * <amount>");
        slotItemSet = section.getString("set", "&a你成功将手上的物品设置到 <slot>");
        slotItemChange = section.getString("change", "&a你成功交换了物品");
        slotItemRemove = section.getString("remove", "&a你成功移除了 <slot> 上的 <item>");

        section = yaml.getConfigurationSection("change-lore");
        changeLoreSuccess = section.getString("success", "&a你成功使用了交换lore的方案&f: &b<plan>");
        changeLoreFail = section.getString("fail", "&c未找到这种交换lore的方案");
    }
}
