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

public class cdTime extends Thread {
    private Thread t;
    private ItemStack itemStack;
    private Player player;
    private int bulletTime;
    private String threadName = "cdTime";
    NamespacedKey STATUS = new NamespacedKey(MoltenWar.instance, "status");

    public cdTime(Player player, ItemStack itemStack, int bulletTime) {
        this.itemStack = itemStack;
        this.player = player;
        this.bulletTime = bulletTime;
    }

    public void run() {
        ItemMeta SBMeta = itemStack.getItemMeta();

        PersistentDataContainer dataContainer = SBMeta.getPersistentDataContainer();
        dataContainer.set(STATUS, PersistentDataType.INTEGER, 0);
        itemStack.setItemMeta(SBMeta);

        double jindu;
        BossBar bossBar = Bukkit.createBossBar(new NamespacedKey(MoltenWar.instance, "a_interesting_bar"), "冷却时间", BarColor.GREEN, BarStyle.SOLID);
        bossBar.addPlayer(player);
        for(int i=1; i<=100; i++){
            jindu = i/100.0;
            try {
                Thread.sleep(bulletTime/100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bossBar.setProgress(jindu);
        }
        bossBar.setVisible(false);
        dataContainer.set(STATUS, PersistentDataType.INTEGER, 1);
        itemStack.setItemMeta(SBMeta);
    }

    public void start () {
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}