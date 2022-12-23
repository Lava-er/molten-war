package club.lavaer.moltenwar.lavaitem;

import club.lavaer.moltenwar.MoltenWar;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

import static club.lavaer.moltenwar.functions.Functions.setNBT;

//模组自定义物品
public class LavaItem {
    public final String TYPE = "物品";  //物品类型

    public int id;  //id
    public String name;  //名称
    public String lore;  //补充
    public Material material;  //材料
    public String displayName;  //显示的名字
    public int itemtype = 0;

    public LavaItem(int id, String name, String lore, Material material){
        this.id = id;
        this.name = name;
        this.lore = ChatColor.BLUE+lore;
        this.material = material;
        this.displayName = ChatColor.GRAY + "["+TYPE+"]" + ChatColor.GREEN + name;
    }

    public LavaItem(int id, String name, String lore, Material material, String type, int itemtype){
        this.id = id;
        this.name = name;
        this.lore = ChatColor.BLUE+lore;
        this.material = material;
        this.displayName = ChatColor.GRAY + "["+type+"]" + ChatColor.GREEN + name;
        this.itemtype = itemtype;
    }

    public void giveItem(Player player, int amount){
        PlayerInventory player_inv = player.getInventory();

        ItemStack SBItem = new ItemStack(material);
        SBItem.setAmount(amount);
        ItemMeta SBMeta = SBItem.getItemMeta();

        //存入NBT
        SBMeta = setNBT(SBMeta,"id",id,PersistentDataType.INTEGER);
        SBMeta = setNBT(SBMeta,"itemtype",itemtype,PersistentDataType.INTEGER);


        SBMeta.setDisplayName(displayName);
        SBMeta.setLore(Arrays.asList(lore));
        SBItem.setItemMeta(SBMeta);

        player_inv.addItem(SBItem);
    }
}
