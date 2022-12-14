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
    public static final String TITLE         = "游玩时 菜单";

    public static final String REDTEAM       = ChatColor.RED + "CT阵营";
    public static final String REDTEAMLORE   = ChatColor.RED + "" + ChatColor.ITALIC + "加入CT阵营";

    public static final String BLUETEAM      = ChatColor.BLUE + "T阵营";
    public static final String BLUETEAMLORE  = ChatColor.BLUE + "" + ChatColor.ITALIC + "加入T阵营";

    public static final String REDSPAWN      = ChatColor.RED + "CT阵营出生点(不要随便动!!!)";
    public static final String REDSPAWNLORE  = ChatColor.RED + "" + ChatColor.ITALIC + "设置CT阵营出生点";

    public static final String BLUESPAWN     = ChatColor.BLUE + "T阵营出生点(不要随便动!!!)";
    public static final String BLUESPAWNLORE = ChatColor.BLUE + "" + ChatColor.ITALIC + "设置T阵营出生点";

    public static final String START         = ChatColor.GREEN + "开始游戏";
    public static final String STARTLORE     = ChatColor.GREEN + "" + ChatColor.ITALIC + "开始游戏";

    public static final String AKM           = ChatColor.GREEN + "AK47";
    public static final String AKMLORE       = ChatColor.GREEN + "" + ChatColor.ITALIC + "选择AK47作为主武器";

    public static final String M416          = ChatColor.GREEN + "M4A1";
    public static final String M416LORE      = ChatColor.GREEN + "" + ChatColor.ITALIC + "选择M4A1作为主武器";

    public static final String AWM           = ChatColor.GREEN + "AWP";
    public static final String AWMLORE       = ChatColor.GREEN + "" + ChatColor.ITALIC + "选择AWP作为主武器";


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
        components = Bukkit.createInventory(player, 27, TITLE);
        owner = player;
        //注册物品
        components.setItem(0, ItemReg(Material.DIAMOND_PICKAXE, REDTEAM, REDTEAMLORE));
        components.setItem(1, ItemReg(Material.TNT_MINECART, BLUETEAM, BLUETEAMLORE));
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
