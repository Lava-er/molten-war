package club.lavaer.moltenwar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Objects;

import static org.bukkit.Bukkit.getWorld;

public class Playing extends Thread{
    private Thread t;
    private C4 c4;
    private BossBar bossBar;
    public void run() {

        bossBar = Bukkit.createBossBar(new NamespacedKey(MoltenWar.instance, "playing_bar"), "游戏时间", BarColor.GREEN, BarStyle.SOLID);
        for(Player players : Objects.requireNonNull(getWorld("world")).getPlayers()){
            bossBar.addPlayer(players);
        }
        for(int i = 120; i>=0; i--){
            bossBar.setProgress(i/120.0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored){}
        }
        CTwin();
    }
    public void C4Plant(){
        bossBar.setVisible(false);
        t.stop();
        c4 = new C4(this);
        c4.start();
    }

    public void Twin(){
        for(Player players : Objects.requireNonNull(getWorld("world")).getPlayers()){
            players.sendMessage(ChatColor.GREEN + "T阵营胜利");
            players.sendTitle(ChatColor.GREEN + "T阵营胜利","");
        }
        pstop();
    }
    public void CTwin(){
        for (Player players : Objects.requireNonNull(getWorld("world")).getPlayers()) {
            players.sendMessage(ChatColor.GREEN + "CT阵营胜利");
            players.sendTitle(ChatColor.GREEN + "CT阵营胜利", "");
        }
        pstop();
    }

    public void start () {
        if (t == null) {
            t = new Thread (this, "playing");
            t.start ();
        }
    }

    public void pstop(){
        bossBar.setVisible(false);
        if(c4 != null) c4.defuse();
        t.stop();
        for(Player players : Objects.requireNonNull(getWorld("world")).getPlayers()){
            players.getInventory().clear();
        }
    }

    public class C4 extends Thread{
        private Thread t;
        private Playing playing;
        BossBar bossBar;
        public C4(Playing playing){
            this.playing = playing;
        }
        public void run() {
            bossBar = Bukkit.createBossBar(new NamespacedKey(MoltenWar.instance, "c4_bar"), "C4爆炸时间", BarColor.RED, BarStyle.SOLID);
            for(Player players : Objects.requireNonNull(getWorld("world")).getPlayers()){
                bossBar.addPlayer(players);
            }
            for(int i = 40; i>=0; i--){
                bossBar.setProgress(i/40.0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored){}
            }
            System.out.println("BOMMMMMMM");
            playing.Twin();
        }
        public void defuse(){
            bossBar.setVisible(false);
            t.stop();
        }
        public void start () {
            if (t == null) {
                t = new Thread(this, "C4");
                t.start();
            }
        }
    }
}
