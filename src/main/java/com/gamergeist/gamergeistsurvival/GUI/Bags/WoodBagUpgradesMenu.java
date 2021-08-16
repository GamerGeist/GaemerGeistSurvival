package com.gamergeist.gamergeistsurvival.GUI.Bags;

import com.gamergeist.gamergeistsurvival.ItemFactory;
import com.gamergeist.gamergeistsurvival.SQL.HashMapCreation;
import com.gamergeist.gamergeistsurvival.SQL.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.List;
import static com.gamergeist.gamergeistsurvival.GamerGeistSurvival.msg;

public class WoodBagUpgradesMenu implements Listener {

    private final SQLGetter sql;
    public WoodBagUpgradesMenu(){
        sql = SQLGetter.getInstance();
    }


    public static String woodBagUpgradesInventoryName = "§cWood Bag §bUpgrades";
    public static int woodBagUpgradesInventorySize = 27;
    public static String woodBagUpgradesLockedName = ChatColor.LIGHT_PURPLE + "§cWood Bag §7[§bLOCKED§7]";
    public static int woodBagUpgradesLockedPrice = 0;

    public static Inventory woodBagUpgradesMenu(Player p) {
        List<Integer> BagStatus = HashMapCreation.getBagMap().get(p.getUniqueId());
        Inventory woodBagUpgradesInventory = Bukkit.createInventory(null, woodBagUpgradesInventorySize, woodBagUpgradesInventoryName);

        if (BagStatus.get(1) >= 1) {
            for (int i = 0; i <= 26; i++) {
                woodBagUpgradesInventory.setItem(i, ItemFactory.generateItemStack("§b", Material.GRAY_STAINED_GLASS_PANE, 1, true));
            }
        } else {
            for (int i = 0; i <= 26; i++) {
                woodBagUpgradesInventory.setItem(i, ItemFactory.generateItemStack("§b", Material.GRAY_STAINED_GLASS_PANE, 1, true));
            }
            woodBagUpgradesInventory.setItem(13, ItemFactory.generateItemStack(woodBagUpgradesLockedName, Material.IRON_BARS, 1, true, Collections.singletonList("§bClick me to Unlock §cWood Bag §bfor " +
                    ChatColor.GREEN + woodBagUpgradesLockedPrice + msg.cur)));
        }

        return woodBagUpgradesInventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        List<Integer> BagStatus = HashMapCreation.getBagMap().get(p.getUniqueId());
        if (e.getView().getTitle().equals(woodBagUpgradesInventoryName)) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.IRON_BARS) {
                if (BagStatus.get(1) >= 1) {
                    p.closeInventory();
                    p.sendMessage("§cWood Bag §balready Unlocked§6!");
                } else {
                    if (sql.getPoints(p.getUniqueId(), "CurrencyTable", "Geisten") >= woodBagUpgradesLockedPrice) {
                        sql.addPoints(p.getUniqueId(), woodBagUpgradesLockedPrice * -1, "CurrencyTable", "Geisten");
                        HashMapCreation.updateBagMap(p,1,1);
                        p.closeInventory();
                        p.sendMessage("§bYou have unlocked §cWood Bag §bLevel §a1§6!");
                        p.openInventory(WoodBagUpgradesMenu.woodBagUpgradesMenu(p));
                    } else {
                        p.sendMessage("§bInsufficient Balance to cover " + ChatColor.GREEN + woodBagUpgradesLockedPrice + msg.cur);
                        p.sendMessage("§bCurrent balance§6: " + ChatColor.GREEN + sql.getPoints(p.getUniqueId(), "CurrencyTable", "Geisten") + msg.cur);
                    }
                }
            }
        }
    }
}
