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

public class LavaGun extends LavaItem{
    public int range; //射程
    public int damage; //伤害
    public int ammunitionLoad; //备弹量
    public int reloadTime; //换弹时间
    public int bulletTime; //上弹时间
    public int status; //枪械状态
    public String ammoType; //子弹种类

    NamespacedKey RANGE = new NamespacedKey(MoltenWar.instance, "range");
    NamespacedKey DAMAGE = new NamespacedKey(MoltenWar.instance, "damage");
    NamespacedKey AMMUNITIONLOAD = new NamespacedKey(MoltenWar.instance, "ammunitionLoad");
    NamespacedKey RELOADTIME = new NamespacedKey(MoltenWar.instance, "reloadTime");
    NamespacedKey AMMOTYPE = new NamespacedKey(MoltenWar.instance, "ammoType");
    NamespacedKey BULLETTIME = new NamespacedKey(MoltenWar.instance, "bulletTime");
    NamespacedKey STATUS = new NamespacedKey(MoltenWar.instance, "status");


    public LavaGun(int id,String name, String lore, Material material, int range, String ammoType, int damage, int ammunitionLoad, int reloadTime,int bulletTime){
        super(id, name, lore, material, "枪械", 1);
        this.ammoType = ammoType;
        this.range = range;
        this.damage = damage;
        this.ammunitionLoad = ammunitionLoad + 1;
        this.reloadTime = reloadTime;
        this.bulletTime = bulletTime;
    }

    @Override
    public void giveItem(Player player, int amount){
        PlayerInventory player_inv = player.getInventory();

        ItemStack SBItem = new ItemStack(material);
        SBItem.setAmount(amount);
        ItemMeta SBMeta = SBItem.getItemMeta();

        PersistentDataContainer dataContainer = SBMeta.getPersistentDataContainer();
        dataContainer.set(ID, PersistentDataType.INTEGER, id);
        dataContainer.set(ITEMTYPE, PersistentDataType.INTEGER, 1);
        dataContainer.set(STATUS, PersistentDataType.INTEGER, 1);
        dataContainer.set(AMMOTYPE, PersistentDataType.STRING, ammoType);
        dataContainer.set(RANGE, PersistentDataType.INTEGER, range);
        dataContainer.set(DAMAGE, PersistentDataType.INTEGER, damage);
        dataContainer.set(AMMUNITIONLOAD , PersistentDataType.INTEGER, ammunitionLoad);
        dataContainer.set(RELOADTIME, PersistentDataType.INTEGER, reloadTime);
        dataContainer.set(BULLETTIME, PersistentDataType.INTEGER, bulletTime);

        SBMeta.setDisplayName(displayName);
        SBMeta.setLore(Arrays.asList(lore,ChatColor.WHITE+"使用"+ChatColor.GREEN+ammoType+"mm"+ChatColor.WHITE+"子弹"));
        SBItem.setItemMeta(SBMeta);

        player_inv.addItem(SBItem);
    }
}
