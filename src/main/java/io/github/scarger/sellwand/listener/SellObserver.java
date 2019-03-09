package io.github.scarger.sellwand.listener;

import com.earth2me.essentials.Worth;
import io.github.scarger.sellwand.SellWand;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SellObserver implements Listener {

    private SellWand plugin;

    public SellObserver(SellWand plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChestHit(PlayerInteractEvent event) {
        if(event.getClickedBlock().getType() == Material.CHEST && event.getItem() != null &&
        event.getItem().getType() == Material.BLAZE_ROD &&
                event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getWandName())) {
            event.setCancelled(true);
            Inventory sellInventory = ((Chest) event.getClickedBlock().getState()).getBlockInventory();
            if (sellInventory.getContents().length > 0) {
                final Worth serverWorth = new Worth(plugin.getEssentials().getDataFolder());
                double sellAmount = 0;
                for (ItemStack itemStack : sellInventory.getContents()) {
                    if(itemStack != null && itemStack.getType() != Material.AIR) {
                        sellAmount += serverWorth.getPrice(itemStack) == null ? 0 : serverWorth.getPrice(itemStack).doubleValue();
                    }
                }
                sellInventory.clear();
                final EconomyResponse depositResponse = plugin.getEconomy().depositPlayer(event.getPlayer(), sellAmount);
                if (depositResponse.transactionSuccess()) {
                    event.getPlayer().sendMessage(ChatColor
                            .translateAlternateColorCodes('&', plugin.getConfig().getString("wand-message"))
                            .replaceAll("%amount%", String.valueOf(sellAmount)));
                } else event.getPlayer().sendMessage(ChatColor.RED + "There was a problem depositing the money into your account..");
            }
            else event.getPlayer().sendMessage(ChatColor.RED + "That chest is empty!");
        }
    }
}
