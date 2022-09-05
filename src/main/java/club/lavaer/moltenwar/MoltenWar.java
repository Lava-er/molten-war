package club.lavaer.moltenwar;

import club.lavaer.moltenwar.menu.Menu;
import club.lavaer.moltenwar.menu.MenusListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.logging.Logger;

public final class MoltenWar extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    public static JavaPlugin instance;
    //加载时
    @Override
    public void onLoad() {
        saveDefaultConfig();
    }
    //启用时
    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new MenusListener(), this);  //注册菜单监听器
        Bukkit.getPluginManager().registerEvents(new WeaponListener(), this);  //注册武器监听器
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    //禁用时
    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    //初始化经济(Vault)
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    //命令触发器
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (command.getName().equals("cd")) { //注册菜单命令
            new Menu(player).open();
            return true;
        }else if(command.getName().equals("test-economy")) { //经济测试命令
            // Lets give the player 1.05 currency (note that SOME economic plugins require rounding!)
            sender.sendMessage(String.format("You have %s", econ.format(econ.getBalance(player.getName()))));
            EconomyResponse r = econ.depositPlayer(player, 1.05);
            if (r.transactionSuccess()) {
                sender.sendMessage(String.format("You were given %s and now have %s", econ.format(r.amount), econ.format(r.balance)));
            } else {
                sender.sendMessage(String.format("An error occured: %s", r.errorMessage));
            }
            return true;
        }else if(command.getName().equals("lavagod")){
            NamespacedKey GODMODE = new NamespacedKey(MoltenWar.instance, "godmode");

            int godmode = 0;
            try{
                godmode = player.getPersistentDataContainer().get(GODMODE, PersistentDataType.INTEGER);
            }catch(NullPointerException ignored){}

            if(godmode == 1){
                player.getPersistentDataContainer().set(GODMODE, PersistentDataType.INTEGER, 0);
                player.sendMessage(ChatColor.RED+"上帝模式已关闭");
            }else{
                player.getPersistentDataContainer().set(GODMODE, PersistentDataType.INTEGER, 1);
                player.sendMessage(ChatColor.RED+"上帝模式已开启");
            }
        }
        return false;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
