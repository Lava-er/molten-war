package club.lavaer.moltenwar.functions;

import club.lavaer.moltenwar.MoltenWar;
import club.lavaer.moltenwar.lavaitem.LavaCaster;
import club.lavaer.moltenwar.lavaitem.LavaItem;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

import static club.lavaer.moltenwar.MoltenWar.friendlyFire;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static org.bukkit.Bukkit.getWorld;

public class Functions {

    //直线发射弹射物
    public static void shootOutAsLine (Player player, int range, int damage, int Hdamage , int recoil){
        World world=player.getWorld();

        int away = recoil;
        if(player.isSprinting())away *= 2;
        if(!player.isOnGround())away *= 2;
        if(!player.isSprinting() && player.isOnGround()) away = 0;
        away *= new Random().nextDouble();
        //得到玩家目光

        Location eyeLoc=player.getEyeLocation();
        eyeLoc.setPitch(eyeLoc.getPitch()+(float) away);
        eyeLoc.setYaw(eyeLoc.getYaw()+(float) away);
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
            for(Entity entity:world.getNearbyEntities(current,0.5,0.5,0.5)) {
                //防止打到自己
                if (entity.getUniqueId() == player.getUniqueId())
                    continue;


                if (entity instanceof Player){
                    Location headLoc = entity.getLocation();
                    headLoc.setY(headLoc.getY() + 1.8);
                    if(current.distance(headLoc) < 0.8) hit(player,Hdamage,entity,"使用 "+player.getInventory().getItemInMainHand().getItemMeta().getDisplayName() +ChatColor.YELLOW +" 爆头枪杀",true);
                    else hit(player,damage,entity,"使用 "+player.getInventory().getItemInMainHand().getItemMeta().getDisplayName() +ChatColor.YELLOW +" 枪杀",false);
                }else hit(player,damage,entity,"使用 "+player.getInventory().getItemInMainHand().getItemMeta().getDisplayName() +ChatColor.YELLOW +" 枪杀",false);

                hit = true;
                break;
            }
            if (hit) break;
            //碰到无法穿透的方块
            if(!current.getBlock().isPassable()){
                world.spawnParticle(Particle.FLAME,current,1);
                break;
            }

            //如果飞出了范围
            if(current.distance(eyeLoc)>range)
                break;
        }
    }
    public static void Grenade(Player player){
        World world=player.getWorld();
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
                bossBar.setProgress(times/60.0);
                world.spawnParticle(Particle.VILLAGER_ANGRY,current,20);
                if(times == 60){
                    //current.getBlock().setType(Material.DIAMOND_BLOCK);
                    for(Entity entity:world.getNearbyEntities(current,6,6,6)) if(entity instanceof LivingEntity) hit(player,18,entity, "炸死了",false);
                    world.spawnParticle(Particle.EXPLOSION_HUGE,current,5);
                    world.playSound(current,Sound.ENTITY_GENERIC_EXPLODE,1,1);
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
                world.spawnParticle(Particle.ASH,current,1);
                world.spawnParticle(Particle.WHITE_ASH,current,1);
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
                if(times > 2) cancel();
                direction.setY(direction.getY() - times*times*gravity);
            }
        }.runTaskTimer(MoltenWar.instance,0L,1L);
    }

    public static void hit (Player player, double damage, Entity entity, String cause, boolean crit){
        World world = player.getWorld();
        Location xx = entity.getLocation();
        Economy econ = MoltenWar.getEconomy();

        NamespacedKey GODMODE = new NamespacedKey(MoltenWar.instance, "godmode");

        int godmode = 0;
        try{
            godmode = entity.getPersistentDataContainer().get(GODMODE, PersistentDataType.INTEGER);
        }catch(NullPointerException ignored){}
        if(godmode == 1) {
            player.sendMessage(ChatColor.RED + "对方处于无敌时间");
            return;
        }
        if(!(entity instanceof LivingEntity)){
            return;
        }
        NamespacedKey TEAM = new NamespacedKey(MoltenWar.instance, "team");
        String team = null;String teams = null;
        try{
            team = player.getPersistentDataContainer().get(TEAM, PersistentDataType.STRING);
            teams = entity.getPersistentDataContainer().get(TEAM, PersistentDataType.STRING);
        }catch (NullPointerException ignored){}
        if(Objects.equals(team, teams) && player.getUniqueId() != entity.getUniqueId() && !friendlyFire){
            player.sendMessage(ChatColor.RED + "不要攻击队友");
            return;
        }

        world.spawnParticle(Particle.CRIT,xx,40);
        world.spawnParticle(Particle.SOUL_FIRE_FLAME,xx,25);
        world.spawnParticle(Particle.DAMAGE_INDICATOR,xx,20);
        player.getWorld().playSound(entity.getLocation(),Sound.ENTITY_PLAYER_HURT,10,1);
        player.playSound(player.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,10,1);

        if(((LivingEntity) entity).getHealth() > damage) {
            if(crit){
                player.sendMessage(ChatColor.YELLOW + "致命伤害！");
            }else{
                player.sendMessage(ChatColor.YELLOW + "命中！");
            }

            //直接减血，防止无敌帧
            ((LivingEntity) entity).setHealth(((LivingEntity) entity).getHealth()-damage);
        }else if(((LivingEntity) entity).getHealth() > 0){  //可以击杀
            player.sendMessage(ChatColor.YELLOW + "击杀！+300 $");
            ((LivingEntity) entity).setHealth(0);
            world.spawnParticle(Particle.TOTEM,xx,20);
            EconomyResponse r = econ.depositPlayer(player, 300);
            for(Player players : player.getWorld().getPlayers()){
                players.sendMessage(ChatColor.GREEN + player.getName() +" "+ ChatColor.YELLOW + cause + " " + ChatColor.GREEN +entity.getName());
            }
        }
    }
    public static void PlayRespawn(Player player){

        new BukkitRunnable(){
            @Override
            public void run() {
                player.getInventory().clear();
                NamespacedKey TEAM = new NamespacedKey(MoltenWar.instance, "team");
                String team = null;
                try{
                    team = player.getPersistentDataContainer().get(TEAM, PersistentDataType.STRING);
                }catch (NullPointerException ignored){}
                new LavaItem(1,"菜单","菜单", Material.CLOCK).giveItem(player,1);
                player.setHealth(20);
                player.setGameMode(GameMode.SURVIVAL);
                if(Objects.equals(team, "red")){
                    Location a = MoltenWar.instance.getConfig().getLocation("redSpawn");
                    player.teleport(a);
                }else if(Objects.equals(team, "blue")){
                    Location a = MoltenWar.instance.getConfig().getLocation("blueSpawn");
                    player.teleport(a);
                }
            }
        }.runTask(MoltenWar.instance);
        new BukkitRunnable(){
            @Override
            public void run() {
                NamespacedKey GODMODE = new NamespacedKey(MoltenWar.instance, "godmode");
                player.getPersistentDataContainer().set(GODMODE, PersistentDataType.INTEGER, 0);
            }
        }.runTaskLater(MoltenWar.instance, 3*20);


    }

    public static Object getNBT(Object object,String key,PersistentDataType p){
        try{
            if(object instanceof ItemStack){
                ItemStack itemStack = (ItemStack) object;
                Object o = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MoltenWar.instance, key), p);
                if ( o==null  && Objects.equals(p,PersistentDataType.INTEGER)) o = 0;
                return o;
            }else if(object instanceof Entity){
                Entity entity = (Entity) object;
                return entity.getPersistentDataContainer().get(new NamespacedKey(MoltenWar.instance, key), p);
            }
        }catch(NullPointerException ignored){
            return  0;
        }
        if(Objects.equals(p,PersistentDataType.STRING)) return null;
        return 0;
    }
    public static ItemMeta setNBT(ItemMeta meta,String key,Object value,PersistentDataType p){
        try{
            meta.getPersistentDataContainer().set(new NamespacedKey(MoltenWar.instance, key), p, value);
        }catch(NullPointerException ignored){}
        return meta;
    }
    public static Entity setNBT(Entity entity,String key,Object value,PersistentDataType p){
        try{
            entity.getPersistentDataContainer().set(new NamespacedKey(MoltenWar.instance, key), p ,value);
        }catch(NullPointerException ignored){}
        return entity;
    }
    public static void speakToAllPlayers(String s){
        for(Player players : Objects.requireNonNull(getWorld("world")).getPlayers()){
            players.sendMessage(s);
        }
    }
    public static boolean runCommand(Player player, String command){
        player.setOp(true);
        boolean flag = player.performCommand(command);
        player.setOp(false);
        return flag;
    }
}
