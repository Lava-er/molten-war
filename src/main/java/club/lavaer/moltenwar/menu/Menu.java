package club.lavaer.moltenwar.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

//菜单
public class Menu {
    public Inventory components;
    public Player owner;
    //创建文本
    public static final String TITLE = "MoltenWar 菜单";

    public static final String GUN = ChatColor.GREEN + "枪械";
    public static final String GUNLORE = ChatColor.GREEN + "" + ChatColor.ITALIC + "获得服务器独特枪械";

    public static final String AMMO = ChatColor.GREEN + "弹药";
    public static final String AMMOLORE = ChatColor.GREEN + "" + ChatColor.ITALIC + "获得子弹";

    //注册物品的方法
    public ItemStack ItemReg(Material material, String name, String lore){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Collections.singletonList(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    //构造函数，并将物品注册进菜单栏
    public Menu(Player player) {
        components = Bukkit.createInventory(player, 9, TITLE);
        owner = player;
        //注册物品
        components.setItem(4, ItemReg(Material.RAW_GOLD_BLOCK,GUN,GUNLORE));
        components.setItem(5, ItemReg(Material.ARROW,AMMO,AMMOLORE));
    }

    //打开菜单
    public void open() {
        owner.openInventory(components);
    }
}
