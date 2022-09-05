package club.lavaer.moltenwar.menu;

import club.lavaer.moltenwar.MoltenWar;
import club.lavaer.moltenwar.functions.Loc30;
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
                /*LavaGun AKM = new LavaGun(2,"AKM","AKM突击步枪", Material.MAGENTA_DYE, 80,"7.62",5,30, 2000,100);
                AKM.giveItem(player, 1);
                LavaGun AWM = new LavaGun(3,"AWM","AWM狙击枪",Material.NETHERITE_SHOVEL, 500,".500",16,5, 2700,2000);
                AWM.giveItem(player, 1);
                LavaGun M416 = new LavaGun(4,"M416","M416突击步枪",Material.LIME_DYE,100,"5.56",5,30, 2400,100);
                M416.giveItem(player, 1);*/
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(Menu.AMMO)){
                NamespacedKey  a762 = new NamespacedKey(MoltenWar.instance, "7.62");
                NamespacedKey  a500 = new NamespacedKey(MoltenWar.instance, ".500");
                NamespacedKey  a556 = new NamespacedKey(MoltenWar.instance, "5.56");

                dataContainer.set(a762, PersistentDataType.INTEGER, 500);
                dataContainer.set(a500, PersistentDataType.INTEGER, 500);
                dataContainer.set(a556, PersistentDataType.INTEGER, 500);
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
                    }else if(team.equals("blue")){
                        Location a = MoltenWar.instance.getConfig().getLocation("blueSpawn");
                        players.setBedSpawnLocation(a,true);
                        ItemStack itemStack = new ItemStack(Material.LEATHER_HELMET);
                        LeatherArmorMeta meta = (LeatherArmorMeta)itemStack.getItemMeta();
                        meta.setColor(Color.BLUE);
                        itemStack.setItemMeta(meta);
                        players.getInventory().setHelmet(itemStack);
                        players.teleport(a);
                    }else{
                        Location a = MoltenWar.instance.getConfig().getLocation("blueSpawn");
                        players.setBedSpawnLocation(a,true);
                        players.teleport(a);
                    }
                    switch (gun) {
                        case "AKM":
                            LavaGun AKM = new LavaGun(2, "AKM", "AKM突击步枪", Material.MAGENTA_DYE, 80, "7.62", 5, 30, 2600, 100);
                            AKM.giveItem(players, 1);
                            break;
                        case "AWM":
                            LavaGun AWM = new LavaGun(3, "AWM", "AWM狙击枪", Material.NETHERITE_SHOVEL, 500, ".500", 16, 5, 2700, 2000);
                            AWM.giveItem(players, 1);
                            break;
                        case "M416": {
                            LavaGun M416 = new LavaGun(4, "M416", "M416突击步枪", Material.LIME_DYE, 100, "5.56", 5, 30, 2400, 100);
                            M416.giveItem(players, 1);
                            break;
                        }
                        default: {
                            new LavaGun(4, "M416", "M416突击步枪", Material.LIME_DYE, 100, "5.56", 4, 30, 2400, 100).giveItem(players, 1);
                            break;
                        }
                    }

                    new LavaCaster(5, "手榴弹","手榴弹，右键丢出",Material.STONE_BUTTON).giveItem(players,2);
                    new LavaSword(6, "军刀","军刀",Material.IRON_SWORD).giveItem(players,1);
                    //players.setMaxHealth(100);
                    players.setGameMode(GameMode.ADVENTURE);
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
