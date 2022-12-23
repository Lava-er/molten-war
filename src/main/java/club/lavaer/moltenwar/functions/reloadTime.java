package club.lavaer.moltenwar.functions;

import club.lavaer.moltenwar.MoltenWar;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class reloadTime extends Thread {
    private Thread t;
    private int ammunitionLoad;
    private int reloadTime;
    private Player player;
    private ItemStack itemStack;
    private String threadName = "reloadTime";
    NamespacedKey STATUS = new NamespacedKey(MoltenWar.instance, "status");

    public reloadTime(Player player, ItemStack itemStack, int ammunitionLoad, int reloadTime) {
        this.itemStack = itemStack;
        this.player = player;
        this.ammunitionLoad = ammunitionLoad;
        this.reloadTime = reloadTime;
    }

    public void run() {
        ItemMeta SBMeta = itemStack.getItemMeta();

        PersistentDataContainer dataContainer = SBMeta.getPersistentDataContainer();
        dataContainer.set(STATUS, PersistentDataType.INTEGER, 0);
        itemStack.setItemMeta(SBMeta);

        double jindu = 0;
        BossBar bossBar = Bukkit.createBossBar(new NamespacedKey(MoltenWar.instance, "reload_bar"), "装弹时间", BarColor.WHITE, BarStyle.SOLID);
        bossBar.addPlayer(player);
        for(int i=1; i<=100; i++){
            if(!player.getInventory().getItemInMainHand().equals(itemStack)) break;
            jindu = i/100.0;
            if(i==30)player.playSound(player.getLocation(), Sound.ITEM_CROSSBOW_LOADING_MIDDLE,20,1);
            try {
                Thread.sleep(reloadTime/100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bossBar.setProgress(jindu);
        }
        bossBar.setVisible(false);
        dataContainer.set(STATUS, PersistentDataType.INTEGER, 1);
        if(jindu == 1){
            int ammoUsed = ammunitionLoad - itemStack.getAmount();
            useAmmo(player, ammoUsed, itemStack);
            itemStack.setItemMeta(SBMeta);
            itemStack.setAmount(ammunitionLoad);
            player.playSound(player.getLocation(),Sound.ITEM_CROSSBOW_LOADING_END,20,1);
        }else{
            itemStack.setItemMeta(SBMeta);
        }

    }

    private void useAmmo(Player player, int ammoUsed, ItemStack itemStack) {
        //求得子弹类型
        ItemMeta meta = itemStack.getItemMeta();
        NamespacedKey AMMO = new NamespacedKey(MoltenWar.instance, "ammoType");
        String ammo = meta.getPersistentDataContainer().get(AMMO, PersistentDataType.STRING);
        //找到玩家弹药数量
        NamespacedKey  ammoss = new NamespacedKey(MoltenWar.instance, ammo);
        int ammosLeft = player.getPersistentDataContainer().get(ammoss, PersistentDataType.INTEGER) - ammoUsed;
        player.getPersistentDataContainer().set(ammoss, PersistentDataType.INTEGER, ammosLeft);

        if(player.getInventory().getItemInMainHand().displayName().equals(itemStack.displayName())){
            player.setExp(0);
            player.setLevel(player.getPersistentDataContainer().get(ammoss, PersistentDataType.INTEGER));
        }
    }

    public void start () {
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}