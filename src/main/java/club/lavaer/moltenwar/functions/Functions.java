package club.lavaer.moltenwar.functions;

import club.lavaer.moltenwar.MoltenWar;
import com.destroystokyo.paper.block.TargetBlockInfo;
import com.destroystokyo.paper.entity.Pathfinder;
import com.destroystokyo.paper.entity.TargetEntityInfo;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static club.lavaer.moltenwar.MoltenWar.redLoc;
import static java.lang.Math.PI;
import static java.lang.Math.abs;

public class Functions {

    //直线发射弹射物
    public static void shootOutAsLine (Player player, int range, int damage){
        World world=player.getWorld();
        //得到玩家目光
        Location eyeLoc=player.getEyeLocation();
        Vector direction= eyeLoc.getDirection().multiply(0.5);
        //current初始值为eyeLoc，即起点
        Location current=eyeLoc.clone();


        boolean hit = false;
        while(true){

            //current向目光方向继续延伸一段
            current.add(direction);
            //召唤粒子效果，使其看得见
            world.spawnParticle(Particle.ASH,current,1);
            world.spawnParticle(Particle.WHITE_ASH,current,1);
            //循环打到的实体
            for(Entity entity:world.getNearbyEntities(current,0.5,0.5,0.5)){
                //防止打到自己
                if(entity.getUniqueId()==player.getUniqueId())
                    continue;
                hit(player,damage,entity);
                hit = true;
                break;
            }
            if (hit) break;
            //碰到无法穿透的方块
            if(!current.getBlock().isPassable())
                break;
            //如果飞出了范围
            if(current.distance(eyeLoc)>range)
                break;
        }
    }
    public static void Grenade(Player player){
        World world=player.getWorld();
        //目光：这里.multiply(0.5)是为了让魔咒看起来连贯，但会影响其速度
        Location eyeLoc=player.getEyeLocation();
        Vector direction= eyeLoc.getDirection().multiply(1);
        //current初始值为eyeLoc，即起点
        Location current=eyeLoc.clone();
        BossBar bossBar = Bukkit.createBossBar(new NamespacedKey(MoltenWar.instance, "a_interesting_bar"), "手雷", BarColor.GREEN, BarStyle.SOLID);
        bossBar.addPlayer(player);

        new BukkitRunnable(){
            int times = 0;
            @Override
            public void run() {
                times++;
                bossBar.setProgress(times/100.0);
                world.spawnParticle(Particle.VILLAGER_ANGRY,current,20);
                if(times == 100){
                    //current.getBlock().setType(Material.DIAMOND_BLOCK);
                    for(Entity entity:world.getNearbyEntities(current,6,6,6)) if(entity instanceof LivingEntity) hit(player,16,entity);
                    world.spawnParticle(Particle.EXPLOSION_HUGE,current,5);
                    bossBar.setVisible(false);
                    cancel();
                }
            }
        }.runTaskTimer(MoltenWar.instance,0L,1L);


        new BukkitRunnable(){
            Block block;
            double times = 0;
            double gravity = 1;


            @Override
            public void run() {

                times += 0.02;
                //current向目光方向继续延伸一段
                current.add(direction);
                //召唤粒子效果，使其看得见
                world.spawnParticle(Particle.VILLAGER_HAPPY,current,2);
                //player.teleport(current);
                //反弹模块
                if(!current.getBlock().isPassable()){

                    cancel();

                    Location loc_1=block.getLocation();
                    Location loc_2=current.getBlock().getLocation();
                    Block target = loc_2.getBlock();
                    //current.getBlock().setType(Material.GOLD_BLOCK);
                    //法线
                    Vector vec=loc_2.subtract(loc_1).toVector();
                    //旋转-高版本：有Vector#rotateAroundAxis()
                    if(abs(vec.getX()) > 1 || abs(vec.getY()) > 1 || abs(vec.getZ())>1){
                        if(vec.getX()>0)vec.setX(1);
                        if(vec.getX()<0)vec.setX(-1);
                        if(vec.getY()>0)vec.setY(1);
                        if(vec.getY()<0)vec.setY(-1);
                        if(vec.getZ()>0)vec.setZ(1);
                        if(vec.getZ()<0)vec.setZ(-1);
                    }
                    if(!(abs(vec.getX())+abs(vec.getY())+abs(vec.getZ()) == 1)){

                        Location locX = new Location(loc_2.getWorld(), target.getX()-vec.getX(), target.getY(), target.getZ());
                        Location locY = new Location(loc_2.getWorld(), target.getX(), target.getY()-vec.getY(), target.getZ());
                        Location locZ = new Location(loc_2.getWorld(), target.getX(), target.getY(), target.getZ()-vec.getZ());
                        if(!locX.getBlock().isPassable()) vec.setX(0);
                        if(!locY.getBlock().isPassable()) vec.setY(0);
                        if(!locZ.getBlock().isPassable()) vec.setZ(0);
                    }
                    if(abs(vec.getX()) > 1 || abs(vec.getY()) > 1 || abs(vec.getZ())>1){
                        System.out.println("vec: "+vec.getX()+" "+vec.getY()+" "+vec.getZ()+" ");
                        direction.setY(direction.getY() - times*times*gravity*2);
                        current.add(direction);
                        cancel();
                    }
                    direction.rotateAroundNonUnitAxis(vec,PI).multiply(-1);
                    //防止魔咒卡在方块里

                    current.add(direction);
                    gravity *= 2;
                }else{
                    //记录上一次经过的空气方块
                    block=current.getBlock();
                }
                if(times > 0.5) cancel();
                direction.setY(direction.getY() - times*times*gravity);
            }
        }.runTaskTimer(MoltenWar.instance,0L,1L);
    }

    public static void hit (Player player, int damage, Entity entity){
        World world = player.getWorld();
        Location xx = entity.getLocation();
        Economy econ = MoltenWar.getEconomy();

        NamespacedKey TEAM = new NamespacedKey(MoltenWar.instance, "team");
        String team = null;String teams = null;
        try{
            team = player.getPersistentDataContainer().get(TEAM, PersistentDataType.STRING);
            teams = entity.getPersistentDataContainer().get(TEAM, PersistentDataType.STRING);
        }catch (NullPointerException ignored){}
        if(Objects.equals(team, teams)){
            return;
        }

        world.spawnParticle(Particle.CRIT,xx,40);
        world.spawnParticle(Particle.SOUL_FIRE_FLAME,xx,25);
        player.getWorld().playSound(entity.getLocation(),Sound.ENTITY_PLAYER_HURT,10,1);
        player.playSound(player.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,10,1);

        if(((LivingEntity) entity).getHealth() > damage) {
            player.sendMessage(ChatColor.YELLOW + "命中！+100 $");
            EconomyResponse r = econ.depositPlayer(player, 100);
            //直接减血，防止无敌帧
            ((LivingEntity) entity).setHealth(((LivingEntity) entity).getHealth()-damage);
        }else{  //可以击杀
            player.sendMessage(ChatColor.YELLOW + "击杀！+2000 $");
            //直接击杀，记录人头归属
            ((LivingEntity) entity).damage(10000, player);
            EconomyResponse r = econ.depositPlayer(player, 2000);
        }
    }
}
