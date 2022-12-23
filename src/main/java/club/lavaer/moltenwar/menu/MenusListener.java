package club.lavaer.moltenwar.menu;

import club.lavaer.moltenwar.MoltenWar;
import club.lavaer.moltenwar.lavaitem.LavaCaster;
import club.lavaer.moltenwar.lavaitem.LavaGun;
import club.lavaer.moltenwar.lavaitem.LavaItem;
import club.lavaer.moltenwar.lavaitem.LavaSword;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

import static club.lavaer.moltenwar.MoltenWar.friendlyFire;

//菜单监听器
public class MenusListener implements Listener {
    //监听点击物品时间
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        InventoryView inv = player.getOpenInventory();
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();

        if (inv.getTitle().equals(Menu.TITLE)) {
            //退出事件，不让玩家从菜单中取出物品
            e.setCancelled(true);

            if (e.getRawSlot() < 0 || e.getRawSlot() > e.getInventory().getSize()) {
                return;
            }

            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null){
                return;
            }

            if (clickedItem.getItemMeta().getDisplayName().equals(Menu.GUN)){
                LavaItem customItem = new LavaItem(1,"菜单","菜单", Material.CLOCK);
                customItem.giveItem(player,1);
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(Menu.AMMO)){
                NamespacedKey  a762 = new NamespacedKey(MoltenWar.instance, "7.62");
                NamespacedKey  a500 = new NamespacedKey(MoltenWar.instance, ".500");
                NamespacedKey  a556 = new NamespacedKey(MoltenWar.instance, "5.56");

                dataContainer.set(a762, PersistentDataType.INTEGER, 500);
                dataContainer.set(a500, PersistentDataType.INTEGER, 500);
                dataContainer.set(a556, PersistentDataType.INTEGER, 500);
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(Menu.FFON)){
                friendlyFire = false;
                player.sendMessage(ChatColor.RED + "队友伤害已关闭");
                new Menu(player).open();
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(Menu.FFOFF)){
                friendlyFire = true;
                player.sendMessage(ChatColor.GREEN + "队友伤害已开启");
                new Menu(player).open();
            }
        }

        if (inv.getTitle().equals(playMenu.TITLE)) {
            //退出事件，不让玩家从菜单中取出物品
            e.setCancelled(true);

            if (e.getRawSlot() < 0 || e.getRawSlot() > e.getInventory().getSize()) {
                return;
            }

            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null){
                return;
            }


            NamespacedKey TEAM = new NamespacedKey(MoltenWar.instance, "team");
            NamespacedKey GUN = new NamespacedKey(MoltenWar.instance, "gun");

            if (clickedItem.getItemMeta().getDisplayName().equals(playMenu.REDTEAM)){
                dataContainer.set(TEAM, PersistentDataType.STRING, "red");

                player.sendMessage(ChatColor.RED + "您已加入红队");
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(playMenu.BLUETEAM)){
                dataContainer.set(TEAM, PersistentDataType.STRING, "blue");

                player.sendMessage(ChatColor.BLUE + "您已加入蓝队");
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(playMenu.REDSPAWN)){
                MoltenWar.instance.getConfig().set("redSpawn", player.getLocation());
                MoltenWar.instance.saveConfig();

                player.sendMessage(ChatColor.RED + "红队重生点已设置");
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(playMenu.BLUESPAWN)){
                MoltenWar.instance.getConfig().set("blueSpawn", player.getLocation());
                MoltenWar.instance.saveConfig();

                player.sendMessage(ChatColor.BLUE + "蓝队重生点已设置");
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(playMenu.START)){
                for(Player players : player.getWorld().getPlayers()){
                    players.getInventory().clear();
                    String team = null;
                    String gun = null;
                    try{
                        team = players.getPersistentDataContainer().get(TEAM, PersistentDataType.STRING);
                    }catch (NullPointerException ignored){}
                    try{
                        gun = players.getPersistentDataContainer().get(GUN, PersistentDataType.STRING);
                    }catch (NullPointerException ignored){}
                    if(Objects.equals(team, "red")){
                        Location a = MoltenWar.instance.getConfig().getLocation("redSpawn");
                        players.setBedSpawnLocation(a,true);
                        ItemStack itemStack = new ItemStack(Material.LEATHER_HELMET);
                        LeatherArmorMeta meta = (LeatherArmorMeta)itemStack.getItemMeta();
                        meta.setColor(Color.RED);
                        itemStack.setItemMeta(meta);
                        players.getInventory().setHelmet(itemStack);
                        players.teleport(a);

                        LavaGun USP = new LavaGun(5, "USP-S", "USP-S手枪", Material.BLUE_DYE, 50, "5.56", 5, 12, 2200, 200,0);
                        USP.giveItem(players, 1);

                        LavaItem Pick = new LavaItem(997, "拆弹器", "拆弹器，可以拆毁C4炸弹", Material.DIAMOND_PICKAXE, "物品", 997);

                    }else if(team.equals("blue")){
                        Location a = MoltenWar.instance.getConfig().getLocation("blueSpawn");
                        players.setBedSpawnLocation(a,true);
                        ItemStack itemStack = new ItemStack(Material.LEATHER_HELMET);
                        LeatherArmorMeta meta = (LeatherArmorMeta)itemStack.getItemMeta();
                        meta.setColor(Color.BLUE);
                        itemStack.setItemMeta(meta);
                        players.getInventory().setHelmet(itemStack);
                        players.teleport(a);

                        LavaGun GLOCK = new LavaGun(6, "GLOCK", "格洛克手枪", Material.GREEN_DYE, 50, "7.62", 5, 20, 2700, 200,0);
                        GLOCK.giveItem(players, 1);

                        LavaItem Bomb = new LavaItem(999, "C4炸弹", "C4炸弹，可以摧毁一片建筑", Material.BREAD, "物品", 999);

                    }else{
                        Location a = MoltenWar.instance.getConfig().getLocation("blueSpawn");
                        players.setBedSpawnLocation(a,true);
                        players.teleport(a);
                    }
                    switch (gun) {
                        case "AKM":
                            LavaGun AKM = new LavaGun(2, "AK47", "AK47突击步枪", Material.MAGENTA_DYE, 80, "7.62", 10, 30, 2500, 100,20);
                            AKM.giveItem(players, 1);
                            break;
                        case "AWM":
                            LavaGun AWM = new LavaGun(3, "AWP", "AWP狙击枪", Material.RED_DYE, 500, ".500", 16, 5, 3700, 2000,0);
                            AWM.giveItem(players, 1);
                            break;
                        case "M416": {
                            LavaGun M416 = new LavaGun(4, "M4A1-S", "M4A1-S突击步枪", Material.LIME_DYE, 100, "5.56", 8, 20, 3100, 100,10);
                            M416.giveItem(players, 1);
                            break;
                        }
                        default: {
                            break;
                        }
                    }

                    new LavaCaster(5, "手榴弹","手榴弹，右键丢出",Material.STONE_BUTTON).giveItem(players,2);
                    new LavaSword(6, "军刀","军刀",Material.IRON_SWORD).giveItem(players,1);
                    //players.setMaxHealth(100);
                    players.setGameMode(GameMode.SURVIVAL);
                    players.setCustomNameVisible(false);
                    players.closeInventory();
                }
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(playMenu.AKM)){
                dataContainer.set(GUN, PersistentDataType.STRING, "AKM");
                player.sendMessage(ChatColor.GREEN + "您已选择AKM");
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(playMenu.AWM)){
                dataContainer.set(GUN, PersistentDataType.STRING, "AWM");
                player.sendMessage(ChatColor.GREEN + "您已选择AWM");
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(playMenu.M416)){
                dataContainer.set(GUN, PersistentDataType.STRING, "M416");
                player.sendMessage(ChatColor.GREEN + "您已选择M416");
            }

        }
    }
}
