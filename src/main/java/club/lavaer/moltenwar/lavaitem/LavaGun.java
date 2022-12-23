package club.lavaer.moltenwar.lavaitem;

import club.lavaer.moltenwar.MoltenWar;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Arrays;

import static club.lavaer.moltenwar.functions.Functions.setNBT;

public class LavaGun extends LavaItem{
    public int range; //射程
    public int damage; //伤害
    public int ammunitionLoad; //备弹量
    public int reloadTime; //换弹时间
    public int bulletTime; //上弹时间
    public String ammoType; //子弹种类
    public int recoil; //稳定性

    public LavaGun(int id,String name, String lore, Material material, int range, String ammoType, int damage, int ammunitionLoad, int reloadTime,int bulletTime,int recoil){
        super(id, name, lore, material, "枪械", 1);
        this.ammoType = ammoType;
        this.range = range;
        this.damage = damage;
        this.ammunitionLoad = ammunitionLoad + 1;
        this.reloadTime = reloadTime;
        this.bulletTime = bulletTime;
        this.recoil = recoil;
    }

    @Override
    public void giveItem(Player player, int amount){
        PlayerInventory player_inv = player.getInventory();

        ItemStack SBItem = new ItemStack(material);
        SBItem.setAmount(amount);
        ItemMeta SBMeta = SBItem.getItemMeta();

        SBMeta = setNBT(SBMeta, "id", id, PersistentDataType.INTEGER);
        SBMeta = setNBT(SBMeta, "itemtype", 1, PersistentDataType.INTEGER);
        SBMeta = setNBT(SBMeta, "status", 1, PersistentDataType.INTEGER);
        SBMeta = setNBT(SBMeta, "ammotype", ammoType, PersistentDataType.STRING);
        SBMeta = setNBT(SBMeta, "range", range, PersistentDataType.INTEGER);
        SBMeta = setNBT(SBMeta, "damage", damage, PersistentDataType.INTEGER);
        SBMeta = setNBT(SBMeta, "ammunitionLoad", ammunitionLoad, PersistentDataType.INTEGER);
        SBMeta = setNBT(SBMeta, "reloadTime", reloadTime, PersistentDataType.INTEGER);
        SBMeta = setNBT(SBMeta, "bulletTime", bulletTime, PersistentDataType.INTEGER);
        SBMeta = setNBT(SBMeta, "recoil", recoil, PersistentDataType.INTEGER);

        SBMeta.setDisplayName(displayName);
        SBMeta.setLore(Arrays.asList(lore,ChatColor.WHITE+"使用"+ChatColor.GREEN+ammoType+"mm"+ChatColor.WHITE+"子弹"));
        SBItem.setItemMeta(SBMeta);

        player_inv.addItem(SBItem);
    }
}
