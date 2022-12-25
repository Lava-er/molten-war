package club.lavaer.moltenwar;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static club.lavaer.moltenwar.MoltenWar.c4Loc;
import static club.lavaer.moltenwar.functions.Functions.PlayRespawn;
import static org.bukkit.Bukkit.getWorld;

public class Playing extends Thread{
    private Thread t;
    private C4 c4;
    private BossBar bossBar;

    public int TCount;
    public int CTCount;

    public boolean flag;

    public Playing(int TCount,int CTCount){
        this.TCount = TCount;
        this.CTCount = CTCount;
        this.flag = false;
    }

    public void run() {

        bossBar = Bukkit.createBossBar(new NamespacedKey(MoltenWar.instance, "playing_bar"), "游戏时间", BarColor.GREEN, BarStyle.SOLID);
        for(Player players : Objects.requireNonNull(getWorld("world")).getPlayers()){
            bossBar.addPlayer(players);
        }
        for(int i = 1200; i>=0 || flag; i--){
            if(i >= 0) bossBar.setProgress(i/1200.0);
            if(CTCount == 0 ) Twin();
            if(TCount == 0 && !flag) CTwin();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored){}
        }
        CTwin();
    }
    public void C4Plant(){
        bossBar.setVisible(false);
        c4 = new C4(this);
        c4.start();
        flag = true;
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
        for(Player players : Objects.requireNonNull(getWorld("world")).getPlayers()){
            PlayRespawn(players);
        }
        System.out.println(c4Loc);
        if(c4Loc != null){
            new BukkitRunnable(){
                @Override
                public void run() {
                    c4Loc.getBlock().setType(Material.AIR);
                }
            }.runTask(MoltenWar.instance);
        }
        if(c4 != null) c4.defuse();
        t.stop();
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
            for(int i = 400; i>=0; i--){
                if(playing.CTCount == 0) Twin();
                bossBar.setProgress(i/400.0);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored){}
            }
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
