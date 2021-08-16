package com.gamergeist.gamergeistsurvival.GUI.Bags;

import com.gamergeist.gamergeistsurvival.ConversationAPI.Conversation_API;
import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import com.gamergeist.gamergeistsurvival.ItemFactory;
import com.gamergeist.gamergeistsurvival.SQL.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class OreBagResourcesMenu implements Listener {

    private static final GamerGeistSurvival plugin = GamerGeistSurvival.getInstance();

    private static SQLGetter sql;

    public OreBagResourcesMenu(){
        sql = SQLGetter.getInstance();
    }


    public static String oreBagInventoryName;

    static {
        assert false;
        oreBagInventoryName = "&cOreBag &eMenu";
    }

    public static int oreBagInventorySize = plugin.getConfig().getInt("Ore-Bag-resources-gui.gui-size");
    public static String oreBagCoalItemName = plugin.getConfig().getString("Ore-Bag-resources-gui.coal-item-name").replaceAll("&", "§");
    public static String oreBagAncientDebrisItemName = ChatColor.LIGHT_PURPLE + "Ancient Debris";
    public static String oreBagGoldItemName = ChatColor.LIGHT_PURPLE + "Gold";
    public static String oreBagIronItemName = ChatColor.LIGHT_PURPLE + "Iron";
    public static String oreBagRedstoneItemName = ChatColor.LIGHT_PURPLE + "Redstone";
    public static String oreBagEmeraldItemName = ChatColor.LIGHT_PURPLE + "Emerald";
    public static String oreBagDiamondItemName = ChatColor.LIGHT_PURPLE + "Diamond";
    public static String oreBagLapisItemName = ChatColor.LIGHT_PURPLE + "Lapis";
    public static String oreBagCopperItemName = ChatColor.LIGHT_PURPLE + "Copper";

    public static Inventory oreBagInv(Player p) {

        Inventory oreBagInventory = Bukkit.createInventory(null, oreBagInventorySize, oreBagInventoryName);


        oreBagInventory.setItem(0, ItemFactory.generateItemStack(oreBagCoalItemName, Material.COAL, 1, true, Collections.singletonList("§bCurrent Coal Amount - " +
                ChatColor.DARK_GREEN + sql.getPoints(p.getUniqueId(), "OreBagTable", "Coal"))));
        oreBagInventory.setItem(1, ItemFactory.generateItemStack(oreBagAncientDebrisItemName, Material.ANCIENT_DEBRIS, 1, true, Collections.singletonList("§bCurrent Ancient Debris Amount - " +
                ChatColor.DARK_GREEN + sql.getPoints(p.getUniqueId(), "OreBagTable", "Ancient_debris"))));
        oreBagInventory.setItem(2, ItemFactory.generateItemStack(oreBagGoldItemName, Material.RAW_GOLD, 1, true, Collections.singletonList("§bCurrent Gold Amount - " +
                ChatColor.DARK_GREEN + sql.getPoints(p.getUniqueId(), "OreBagTable", "Gold"))));
        oreBagInventory.setItem(3, ItemFactory.generateItemStack(oreBagIronItemName, Material.RAW_IRON, 1, true, Collections.singletonList("§bCurrent Iron Amount - " +
                ChatColor.DARK_GREEN + sql.getPoints(p.getUniqueId(), "OreBagTable", "Iron"))));
        oreBagInventory.setItem(4, ItemFactory.generateItemStack(oreBagRedstoneItemName, Material.REDSTONE, 1, true, Collections.singletonList("§bCurrent Redstone Amount - " +
                ChatColor.DARK_GREEN + sql.getPoints(p.getUniqueId(), "OreBagTable", "Redstone"))));
        oreBagInventory.setItem(5, ItemFactory.generateItemStack(oreBagEmeraldItemName, Material.EMERALD, 1, true, Collections.singletonList("§bCurrent Emerald Amount - " +
                ChatColor.DARK_GREEN + sql.getPoints(p.getUniqueId(), "OreBagTable", "Emerald"))));
        oreBagInventory.setItem(6, ItemFactory.generateItemStack(oreBagDiamondItemName, Material.DIAMOND, 1, true, Collections.singletonList("§bCurrent Diamond Amount - " +
                ChatColor.DARK_GREEN + sql.getPoints(p.getUniqueId(), "OreBagTable", "Diamond"))));
        oreBagInventory.setItem(7, ItemFactory.generateItemStack(oreBagLapisItemName, Material.LAPIS_LAZULI, 1, true, Collections.singletonList("§bCurrent Lapis Amount - " +
                ChatColor.DARK_GREEN + sql.getPoints(p.getUniqueId(), "OreBagTable", "Lapis"))));
        oreBagInventory.setItem(8, ItemFactory.generateItemStack(oreBagCopperItemName, Material.RAW_COPPER, 1, true, Collections.singletonList("§bCurrent Copper Amount - " +
                ChatColor.DARK_GREEN + sql.getPoints(p.getUniqueId(), "OreBagTable", "Copper"))));

        return oreBagInventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equals(oreBagInventoryName)) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.COAL) {
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(oreBagCoalItemName)) {
                    Conversation_API.SendInputMessage(player);
                    player.closeInventory();
                }
            }

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.ANCIENT_DEBRIS) {
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(oreBagAncientDebrisItemName)) {
                    Conversation_API.SendInputMessage(player);
                    player.closeInventory();
                }
            }

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.RAW_GOLD) {
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(oreBagGoldItemName)) {
                    Conversation_API.SendInputMessage(player);
                    player.closeInventory();
                }
            }

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.RAW_IRON) {
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(oreBagIronItemName)) {
                    Conversation_API.SendInputMessage(player);
                    player.closeInventory();
                }
            }

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.REDSTONE) {
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(oreBagRedstoneItemName)) {
                    Conversation_API.SendInputMessage(player);
                    player.closeInventory();
                }
            }

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.EMERALD) {
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(oreBagEmeraldItemName)) {
                    Conversation_API.SendInputMessage(player);
                    player.closeInventory();
                }
            }

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.DIAMOND) {
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(oreBagDiamondItemName)) {
                    Conversation_API.SendInputMessage(player);
                    player.closeInventory();
                }
            }

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.LAPIS_LAZULI) {
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(oreBagLapisItemName)) {
                    Conversation_API.SendInputMessage(player);
                    player.closeInventory();
                }
            }

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.RAW_COPPER) {
                ItemMeta meta = e.getCurrentItem().getItemMeta();
                if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(oreBagCopperItemName)) {
                    Conversation_API.SendInputMessage(player);
                    player.closeInventory();
                }
            }
        }
    }
}
