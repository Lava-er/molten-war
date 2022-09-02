package club.lavaer.moltenwar.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class playMenu {
    public Inventory components;
    public Player owner;
    //创建文本
    public static final String TITLE = "游玩时 菜单";

    public static final String REDTEAM = ChatColor.RED + "红队";
    public static final String REDTEAMLORE = ChatColor.RED + "" + ChatColor.ITALIC + "加入红队";

    public static final String BLUETEAM = ChatColor.BLUE + "蓝队";
    public static final String BLUETEAMLORE = ChatColor.BLUE + "" + ChatColor.ITALIC + "加入蓝队";

    public static final String REDSPAWN = ChatColor.RED + "红队出生点";
    public static final String REDSPAWNLORE = ChatColor.RED + "" + ChatColor.ITALIC + "设置红队出生点";

    public static final String BLUESPAWN = ChatColor.BLUE + "蓝队出生点";
    public static final String BLUESPAWNLORE = ChatColor.BLUE + "" + ChatColor.ITALIC + "设置蓝队出生点";

    public static final String START = ChatColor.GREEN + "开始游戏";
    public static final String STARTLORE = ChatColor.GREEN + "" + ChatColor.ITALIC + "开始游戏";

    public static final String AKM = ChatColor.GREEN + "AKM";
    public static final String AKMLORE = ChatColor.GREEN + "" + ChatColor.ITALIC + "选择AKM作为主武器";

    public static final String M416 = ChatColor.GREEN + "M416";
    public static final String M416LORE = ChatColor.GREEN + "" + ChatColor.ITALIC + "选择M416作为主武器";

    public static final String AWM = ChatColor.GREEN + "AWM";
    public static final String AWMLORE = ChatColor.GREEN + "" + ChatColor.ITALIC + "选择AWM作为主武器";


    //注册物品的方法
    public ItemStack ItemReg(Material material, String name, String lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Collections.singletonList(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    //构造函数，并将物品注册进菜单栏
    public playMenu(Player player) {
        components = Bukkit.createInventory(player, 18, TITLE);
        owner = player;
        //注册物品
        components.setItem(0, ItemReg(Material.RED_CANDLE, REDTEAM, REDTEAMLORE));
        components.setItem(1, ItemReg(Material.BLUE_CANDLE, BLUETEAM, BLUETEAMLORE));
        components.setItem(2, ItemReg(Material.RED_BED, REDSPAWN, REDSPAWNLORE));
        components.setItem(3, ItemReg(Material.BLUE_BED, BLUESPAWN, BLUESPAWNLORE));
        components.setItem(4, ItemReg(Material.LAVA_BUCKET, START, STARTLORE));

        components.setItem(9, ItemReg(Material.MAGENTA_DYE, AKM, AKMLORE));
        components.setItem(10, ItemReg(Material.LIME_DYE, M416, M416LORE));
        components.setItem(11, ItemReg(Material.NETHERITE_SHOVEL, AWM, AWMLORE));
    }

    //打开菜单
    public void open() {
        owner.openInventory(components);
    }
}
