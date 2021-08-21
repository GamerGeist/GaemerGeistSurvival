package com.gamergeist.gamergeistsurvival.GUI.Bags;

import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import com.gamergeist.gamergeistsurvival.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class BagsMenu implements Listener {

    static GamerGeistSurvival plugin;


    public BagsMenu(GamerGeistSurvival bags) {
        plugin = bags;
    }

    public static String bagsMenuInventoryName = "Bags Shop";
    public static int bagsMenuInventorySize = 9;
    public static String bagsMenuOreBagItemName = ChatColor.LIGHT_PURPLE + "Ore Bag";
    public static String bagsMenuWoodBagItemName = ChatColor.LIGHT_PURPLE + "Wood Bag";
    public static String bagsMenuFarmBagItemName = ChatColor.LIGHT_PURPLE + "Farm Bag";

    public static Inventory bagsMenu(Player p) {

        Inventory bagsMenuInventory = Bukkit.createInventory(null, bagsMenuInventorySize, bagsMenuInventoryName);

        bagsMenuInventory.setItem(2, ItemFactory.generateItemStack(bagsMenuOreBagItemName, Material.CHEST, 1, true, Collections.singletonList("§bClick to open §cOre bag §bUpgrades Inventory§6.")));
        bagsMenuInventory.setItem(4, ItemFactory.generateItemStack(bagsMenuWoodBagItemName, Material.CHEST, 1, true, Collections.singletonList("Click me to buy a Wood Bag for 0 Test Coins!")));
        bagsMenuInventory.setItem(6, ItemFactory.generateItemStack(bagsMenuFarmBagItemName, Material.CHEST, 1, true, Collections.singletonList("Click me to buy a Farm Bag for 0 Test Coins!")));

        return bagsMenuInventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equals(bagsMenuInventoryName)) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.CHEST) {
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(bagsMenuOreBagItemName)) {
                    p.openInventory(OreBagUpgradesMenu.oreBagUpgradesMenu(p));
                }
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(bagsMenuWoodBagItemName)) {
                    p.openInventory(WoodBagUpgradesMenu.woodBagUpgradesMenu(p));
                }
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(bagsMenuFarmBagItemName)) {
                    p.openInventory(FarmBagUpgradesMenu.farmBagUpgradesMenu(p));
                }
            }
        }
    }
}
