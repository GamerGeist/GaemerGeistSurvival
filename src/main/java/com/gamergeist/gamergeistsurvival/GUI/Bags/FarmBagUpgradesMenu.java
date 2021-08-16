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

public class FarmBagUpgradesMenu implements Listener {

    private final SQLGetter sql;
    public FarmBagUpgradesMenu(){
        sql = SQLGetter.getInstance();
    }


    public static String farmBagUpgradesInventoryName = "§cFarm Bag §bUpgrades";
    public static int farmBagUpgradesInventorySize = 27;
    public static String farmBagUpgradesLockedName = ChatColor.LIGHT_PURPLE + "§cFarm Bag §7[§bLOCKED§7]";
    public static int farmBagUpgradesLockedPrice = 0;

    public static Inventory farmBagUpgradesMenu(Player p) {
        List<Integer> BagStatus = HashMapCreation.getBagMap().get(p.getUniqueId());
        Inventory farmBagUpgradesInventory = Bukkit.createInventory(null, farmBagUpgradesInventorySize, farmBagUpgradesInventoryName);

        if (BagStatus.get(2) >= 1) {
            for (int i = 0; i <= 26; i++) {
                farmBagUpgradesInventory.setItem(i, ItemFactory.generateItemStack("§b", Material.GRAY_STAINED_GLASS_PANE, 1, true));
            }
        } else {
            for (int i = 0; i <= 26; i++) {
                farmBagUpgradesInventory.setItem(i, ItemFactory.generateItemStack("§b", Material.GRAY_STAINED_GLASS_PANE, 1, true));
            }
            farmBagUpgradesInventory.setItem(13, ItemFactory.generateItemStack(farmBagUpgradesLockedName, Material.IRON_BARS, 1, true, Collections.singletonList("§bClick me to Unlock §cFarm Bag §bfor " +
                    ChatColor.GREEN + farmBagUpgradesLockedPrice + msg.cur)));
        }

        return farmBagUpgradesInventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        List<Integer> BagStatus = HashMapCreation.getBagMap().get(p.getUniqueId());
        if (e.getView().getTitle().equals(farmBagUpgradesInventoryName)) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.IRON_BARS) {
                if (BagStatus.get(2) >= 1) {
                    p.closeInventory();
                    p.sendMessage("§cFarm Bag §balready Unlocked§6!");
                } else {
                    if (sql.getPoints(p.getUniqueId(), "CurrencyTable", "Geisten") >= farmBagUpgradesLockedPrice) {
                        sql.addPoints(p.getUniqueId(), farmBagUpgradesLockedPrice * -1, "CurrencyTable", "Geisten");
                        HashMapCreation.updateBagMap(p,1,2);
                        p.closeInventory();
                        p.sendMessage("§bYou have unlocked §cFarm Bag §bLevel §a1§6!");
                        p.openInventory(FarmBagUpgradesMenu.farmBagUpgradesMenu(p));
                    } else {
                        p.sendMessage("§bInsufficient Balance to cover " + ChatColor.GREEN + farmBagUpgradesLockedPrice + msg.cur);
                        p.sendMessage("§bCurrent balance§6: " + ChatColor.GREEN + sql.getPoints(p.getUniqueId(), "CurrencyTable", "Geisten") + msg.cur);
                    }
                }
            }
        }
    }

}
