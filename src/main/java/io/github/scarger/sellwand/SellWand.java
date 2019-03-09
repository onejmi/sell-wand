package io.github.scarger.sellwand;

import com.earth2me.essentials.Essentials;
import io.github.scarger.sellwand.command.SellWandCmd;
import io.github.scarger.sellwand.listener.SellObserver;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getServer;

public class SellWand extends JavaPlugin {

    private final String WAND_NAME = ChatColor.DARK_PURPLE + "Sell Wand";
    private Economy economy;
    private Essentials essentials;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        saveDefaultConfig();
        getCommand("sellwand").setExecutor(new SellWandCmd(this));
        getServer().getPluginManager().registerEvents(new SellObserver(this), this);
    }

    @Override
    public void onDisable() {}

    public ItemStack getSellWand(int amount) {
        ItemStack wand = new ItemStack(Material.BLAZE_ROD, amount);
        ItemMeta wandMeta = wand.getItemMeta();
        wandMeta.setDisplayName(WAND_NAME);
        wand.setItemMeta(wandMeta);
        return wand;
    }

    public String getWandName() {
        return WAND_NAME;
    }

    public Economy getEconomy() {
        return economy;
    }

    public Essentials getEssentials() {
        return essentials;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
