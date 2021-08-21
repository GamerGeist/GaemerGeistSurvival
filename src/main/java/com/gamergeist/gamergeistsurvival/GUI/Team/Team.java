package com.gamergeist.gamergeistsurvival.GUI.Team;

import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import com.gamergeist.gamergeistsurvival.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class Team implements Listener {

    private static GamerGeistSurvival plugin;
    public Team(GamerGeistSurvival plugin) {
        this.plugin = plugin;
    }

    private static final int teamMenuInvSize = 9;
    private static final String teamMenuInvName = "§bTeam Menu";
    private static final String kickName = "Kick Player";
    private static final String inviteName = "Invite Player";
    private static final String promoteName = "Promote Player";
    private static final String demoteName = "Demote Player";

    public static Inventory teamMenu(Player p) {
        Inventory teamMenuInventory = Bukkit.createInventory(null, teamMenuInvSize, teamMenuInvName);

        for (int i = 0; i <= teamMenuInvSize - 1; i++) {
            teamMenuInventory.setItem(i, ItemFactory.generateItemStack("§b", Material.GRAY_STAINED_GLASS_PANE, 1, true));
        }

        teamMenuInventory.setItem(0, ItemFactory.generateItemStack(kickName, Material.LAPIS_LAZULI, 1, true));
        teamMenuInventory.setItem(1, ItemFactory.generateItemStack(inviteName, Material.REDSTONE, 1, true));
        teamMenuInventory.setItem(2, ItemFactory.generateItemStack(promoteName, Material.DIAMOND, 1, true));
        teamMenuInventory.setItem(3, ItemFactory.generateItemStack(demoteName, Material.EMERALD, 1, true));

        return teamMenuInventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(teamMenuInvName)) {
            ItemMeta meta = Objects.requireNonNull(e.getCurrentItem()).getItemMeta();
            if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(kickName)) {
                // What you want to do
            }
            if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(inviteName)) {
                // What you want to do
            }
            if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(promoteName)) {
                // What you want to do
            }
            if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(demoteName)) {
                // What you want to do
            }
        }
    }
}
