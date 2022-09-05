package club.lavaer.moltenwar;

import club.lavaer.moltenwar.functions.cdTime;
import club.lavaer.moltenwar.functions.reloadTime;
import club.lavaer.moltenwar.lavaitem.LavaCaster;
import club.lavaer.moltenwar.menu.playMenu;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import static club.lavaer.moltenwar.functions.Functions.*;

//武器监听器
public class WeaponListener implements Listener {
    //发射弹射物
    @EventHandler
    public void Shoot(PlayerInteractEvent event) throws java.lang.NullPointerException {
        if ((event.getHand() == EquipmentSlot.HAND) && event.hasItem()) {

            ItemStack itemStack = event.getItem();
            ItemMeta meta = itemStack.getItemMeta();
            Player player = event.getPlayer();
            NamespacedKey ITEMTYPE = new NamespacedKey(MoltenWar.instance, "itemType");
            NamespacedKey STATUS = new NamespacedKey(MoltenWar.instance, "status");
            NamespacedKey ID = new NamespacedKey(MoltenWar.instance, "id");

            int itemType = 0;
            int status = 0;
            int id = 100;
            try{
                itemType = meta.getPersistentDataContainer().get(ITEMTYPE, PersistentDataType.INTEGER);

                id = meta.getPersistentDataContainer().get(ID, PersistentDataType.INTEGER);

                status = meta.getPersistentDataContainer().get(STATUS, PersistentDataType.INTEGER);
            }catch(NullPointerException ignored){}

            switch (event.getAction()) {
                case LEFT_CLICK_AIR:
                case LEFT_CLICK_BLOCK:
                    if(itemType == 1 && status == 1){
                        NamespacedKey AMMOS = new NamespacedKey(MoltenWar.instance, "ammunitionLoad");
                        int ammos = meta.getPersistentDataContainer().get(AMMOS, PersistentDataType.INTEGER);
                        NamespacedKey RETIME = new NamespacedKey(MoltenWar.instance, "reloadTime");
                        int retime = meta.getPersistentDataContainer().get(RETIME, PersistentDataType.INTEGER);
                        NamespacedKey AMMO = new NamespacedKey(MoltenWar.instance, "ammoType");
                        String ammo = meta.getPersistentDataContainer().get(AMMO, PersistentDataType.STRING);

                        int ammoUsed = ammos - itemStack.getAmount();
                        NamespacedKey  ammoss = new NamespacedKey(MoltenWar.instance, ammo);
                        int ammosLeft = player.getPersistentDataContainer().get(ammoss, PersistentDataType.INTEGER) - ammoUsed;

                        if(itemStack.getAmount() < ammos && ammosLeft > 0){
                            player.playSound(player.getLocation(), Sound.ITEM_CROSSBOW_LOADING_START,20,1);
                            reloadTime T1 = new reloadTime(player, itemStack ,ammos,retime);
                            T1.start();
                        }
                        event.setCancelled(true);
                    }
                    break;
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    if(itemType == 1 && itemStack.getAmount() > 1 && status == 1){
                        player.getWorld().playSound(player.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,1,1);
                        NamespacedKey RANGE = new NamespacedKey(MoltenWar.instance, "range");
                        NamespacedKey DAMAGE = new NamespacedKey(MoltenWar.instance, "damage");
                        NamespacedKey AMMOTIME = new NamespacedKey(MoltenWar.instance, "bulletTime");
                        int damage = meta.getPersistentDataContainer().get(DAMAGE, PersistentDataType.INTEGER);
                        int range = meta.getPersistentDataContainer().get(RANGE, PersistentDataType.INTEGER);
                        int ammotime = meta.getPersistentDataContainer().get(AMMOTIME, PersistentDataType.INTEGER);
                        itemStack.setAmount(itemStack.getAmount()-1);
                        shootOutAsLine(player,range,damage);
                        if(ammotime != 0){
                            cdTime T2 = new cdTime(player, itemStack ,ammotime);
                            T2.start();
                        }

                        event.setCancelled(true);
                    }
                    if(itemType == 1 && itemStack.getAmount() == 1 && status == 1){
                        player.sendMessage(ChatColor.DARK_RED + "弹夹已空");
                        //Grenade(player,100);
                    }
                    if(itemType == 0 && id == 1){
                        new playMenu(player).open();
                    }
                    if(itemType == 2 && id == 5){
                        Grenade(player);
                        itemStack.setAmount(itemStack.getAmount()-1);
                    }
                    break;
                default:
                    break;
            }
        }
    }
    //显示子弹数
    @EventHandler
    public void ammoDisplay(PlayerItemHeldEvent e){
        Player player = e.getPlayer();
        //取得手持物品
        int slot = e.getNewSlot();
        ItemStack itemStack = player.getInventory().getItem(slot);
        ItemMeta meta = null;
        if(itemStack != null) meta = itemStack.getItemMeta();
        //取得物品种类标签
        NamespacedKey ITEMTYPE = new NamespacedKey(MoltenWar.instance, "itemType");
        int itemType = 0;
        try{
            itemType = meta.getPersistentDataContainer().get(ITEMTYPE, PersistentDataType.INTEGER);
        }catch(NullPointerException ignored){}
        //如果是一式武器，显示备弹量
        if(itemType == 1){
            //取得子弹名称
            NamespacedKey AMMOTYPE = new NamespacedKey(MoltenWar.instance, "ammoType");
            String ammoType = meta.getPersistentDataContainer().get(AMMOTYPE, PersistentDataType.STRING);
            //取得玩家备弹量，显示
            NamespacedKey AMMOOFPLAYER = new NamespacedKey(MoltenWar.instance, ammoType);
            int ammos = player.getPersistentDataContainer().get(AMMOOFPLAYER, PersistentDataType.INTEGER);
            player.setExp(0);
            player.setLevel(ammos);
        }else{
            player.setLevel(0);
        }
    }
    @EventHandler
    public void playerRespawn(PlayerRespawnEvent e){

        Player player = e.getPlayer();
        player.sendMessage(ChatColor.RED + "你死了！等待五秒复活");
        player.setGameMode(GameMode.SPECTATOR);

        NamespacedKey GODMODE = new NamespacedKey(MoltenWar.instance, "godmode");
        player.getPersistentDataContainer().set(GODMODE, PersistentDataType.INTEGER, 1);
        new BukkitRunnable(){
            @Override
            public void run() {
                PlayRespawn(player);
                player.setGameMode(GameMode.ADVENTURE);
            }
        }.runTaskLater(MoltenWar.instance, 5*20);
    }
    @EventHandler
    public void melee (EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player){
            Entity entity = event.getEntity();
            Player player = (Player) event.getDamager();
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            ItemMeta meta = itemStack.getItemMeta();

            NamespacedKey ITEMTYPE = new NamespacedKey(MoltenWar.instance, "itemType");
            int itemType = 0;
            try{
                itemType = meta.getPersistentDataContainer().get(ITEMTYPE, PersistentDataType.INTEGER);
            }catch(NullPointerException ignored){}

            if(itemType == 3 && player.getNearbyEntities(3,3,3).contains(entity)){
                meta.getPersistentDataContainer().set(ITEMTYPE, PersistentDataType.INTEGER, -1);
                itemStack.setItemMeta(meta);
                hit(player, 10, entity,"刀了");
                meta.getPersistentDataContainer().set(ITEMTYPE, PersistentDataType.INTEGER, 3);
                itemStack.setItemMeta(meta);
                entity.getWorld().spawnParticle(Particle.SWEEP_ATTACK,entity.getLocation(),1);
                event.setCancelled(true);
                return;
            }

            hit(player, event.getDamage(), entity, "击杀了");

            event.setCancelled(true);
        }
    }
}
