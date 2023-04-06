package com.soulflame.dragonslotextension.filemanager.config;

import com.soulflame.dragonslotextension.filemanager.FileManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Message extends FileManager {

    public String prefix;
    public List<String> help;
    public String noChildCommand;
    public String dontHavePermission;
    public String commandError;
    public String cantUseInConsole;
    public String cantTake;
    public String cantSwap;
    public String cantDrop;
    public String cantFastMove;
    public String cantSwapHand;
    public String checkMaterial;
    public String itemError;
    public String haveErrorItem;
    public String equipSuccess;
    public String equipFail;
    public String equipNone;
    public String takeNotice;
    public String saveEquipItem;
    public String playerOffline;
    public String slotItemAir;
    public String slotItemNotAir;
    public String slotItemGet;
    public String slotItemSet;
    public String slotItemChange;
    public String slotItemRemove;
    public String swapItem;
    public String itemInHandAir;
    public String fileError;
    public String debugMapping;
    public List<String> debugEquipChance;
    public String debugSwapItem;
    public String reload;

    /**
     * 读取配置文件
     *
     * @param folder 配置文件夹
     * @param name   配置文件名
     */
    public Message(File folder, String name) {
        super(folder, name);
    }

    @Override
    protected void loadData() {
        YamlConfiguration yaml = getYaml();
        prefix = yaml.getString("prefix", "&7[&6Dragon&bSlot&eExtension&7] ");
        help = yaml.getStringList("help");
        noChildCommand = yaml.getString("no-child-command", "&c该指令不存在: ");
        dontHavePermission = yaml.getString("dont-have-permission", "&c执行该指令需要权限: ");
        commandError = yaml.getString("command-args-error", "&c指令参数错误或不完整,请检查是否输错了指令: ");
        cantUseInConsole = yaml.getString("cant-use-in-console", "&c后台无法执行该指令");
        cantTake = yaml.getString("cant-take", "&4你无法拿取此物品");
        cantSwap = yaml.getString("cant-swap", "&4你无法切换到此物品栏");
        cantFastMove = yaml.getString("cant-fast-move", "&4你无法快速移动物品");
        cantDrop = yaml.getString("cant-drop", "&4你无法丢弃此物品");
        cantSwapHand = yaml.getString("cant-swap-hand", "&4你无法切换此物品到主副手");
        checkMaterial = yaml.getString("check-item-hand", "&a你手里物品的材质为: <texture>");
        itemError = yaml.getString("get-item-error", "&4物品获取失败, 请检查配置是否出错");
        haveErrorItem = yaml.getString("have-error-item", "&4身上存在违规物品, 已清除");
        equipSuccess = yaml.getString("equip-success", "&a你成功装备了 <item>");
        equipFail = yaml.getString("equip-fail", "&c装备失败, 物品 <item> 已被销毁");
        equipNone = yaml.getString("equip-none", "&c请放入要装备的物品");
        takeNotice = yaml.getString("take-notice", "&c请在 <time> 秒内, 连续点击两次以拆卸物品");
        saveEquipItem = yaml.getString("equip-save", "&a已保留装备, 保护符已扣除");
        playerOffline = yaml.getString("player-not-online", "&c该玩家不在线, 无法执行指令");
        slotItemAir = yaml.getString("slot-item-air", "&c该槽位物品为空");
        slotItemNotAir = yaml.getString("slot-item-not-air", "&c该槽位物品不为空");
        slotItemGet = yaml.getString("slot-item-get", "&a你得到了玩家 <target> 的 <slot> 里的物品 <item> * <amount>");
        slotItemSet = yaml.getString("slot-item-set", "&a你成功将手上的物品设置到 <slot>");
        slotItemChange = yaml.getString("slot-item-change", "&a你成功交换了物品");
        slotItemRemove = yaml.getString("slot-item-remove", "&a你成功移除了 <slot> 上的 <item>");
        swapItem = yaml.getString("swap-item", "&a你成功使用了交换方案&f: &b<plan>");
        itemInHandAir = yaml.getString("item-in-hand-air", "&c你手上没有任何物品");
        fileError = yaml.getString("file-error", "&c配置出错, 请排查错误");
        debugMapping = yaml.getString("debug-format.mapping", "&6你点击了&f: &b<id>");
        debugEquipChance = yaml.getStringList("debug-format.equip-chance");
        debugSwapItem = yaml.getString("debug-format.swap-item", "&6你按下了按键&f: &b<key>");
        reload = yaml.getString("reload", "&a插件重载成功");
    }
}
