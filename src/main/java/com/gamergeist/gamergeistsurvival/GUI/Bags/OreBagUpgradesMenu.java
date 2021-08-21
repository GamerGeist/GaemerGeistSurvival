package com.gamergeist.gamergeistsurvival.GUI.Bags;

import com.gamergeist.gamergeistsurvival.utils.ItemFactory;
import com.gamergeist.gamergeistsurvival.SQL.HashMaps.HashmapBags;
import com.gamergeist.gamergeistsurvival.SQL.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.gamergeist.gamergeistsurvival.GamerGeistSurvival.msg;

public class OreBagUpgradesMenu implements Listener {

    private final SQLGetter sql;
    private static final List<OreBagUpgrades> upgrades = new ArrayList<>();

    public OreBagUpgradesMenu() {
        sql = SQLGetter.getInstance();

        OreBagUpgrades item1 = new OreBagUpgrades(Material.STONE, "§eLevel §a1", Collections.singletonList("§6+§a1 §bOre Bag§6: "));
        OreBagUpgrades item2 = new OreBagUpgrades(Material.STONE, "§eLevel §a2", Collections.singletonList("§bUndecided"));
        OreBagUpgrades item3 = new OreBagUpgrades(Material.STONE, "§eLevel §a3", Collections.singletonList("§bUndecided"));
        OreBagUpgrades item4 = new OreBagUpgrades(Material.STONE, "§eLevel §a4", Collections.singletonList("§bUndecided"));
        OreBagUpgrades item5 = new OreBagUpgrades(Material.STONE, "§eLevel §a5", Collections.singletonList("§bUndecided"));
        OreBagUpgrades item6 = new OreBagUpgrades(Material.STONE, "§eLevel §a6", Collections.singletonList("§bUndecided"));
        OreBagUpgrades item7 = new OreBagUpgrades(Material.STONE, "§eLevel §a7", Collections.singletonList("§bUndecided"));
        OreBagUpgrades item8 = new OreBagUpgrades(Material.STONE, "§eLevel §a8", Collections.singletonList("§bUndecided"));
        OreBagUpgrades item9 = new OreBagUpgrades(Material.STONE, "§eLevel §a9", Collections.singletonList("§bUndecided"));


        upgrades.add(item1);
        upgrades.add(item2);
        upgrades.add(item3);
        upgrades.add(item4);
        upgrades.add(item5);
        upgrades.add(item6);
        upgrades.add(item7);
        upgrades.add(item8);
        upgrades.add(item9);


    }


    public static String oreBagUpgradesInventoryName = "§cOre Bag §bUpgrades";
    public static int oreBagUpgradesInventorySize = 27;
    public static String oreBagUpgradesLockedName = ChatColor.LIGHT_PURPLE + "§cOre Bag §7[§bLOCKED§7]";
    public static int oreBagUpgradesLockedPrice = 0;
    public static String oreBagUpgradesResourcesName = "§cOre Bag §bResources";

    public static Inventory oreBagUpgradesMenu(Player p) {
        List<Integer> BagStatus = HashmapBags.getBagMap().get(p.getUniqueId());
        Inventory oreBagUpgradesInventory = Bukkit.createInventory(null, oreBagUpgradesInventorySize, oreBagUpgradesInventoryName);
        for (int i = 0; i <= 26; i++) {
            oreBagUpgradesInventory.setItem(i, ItemFactory.generateItemStack("§b", Material.GRAY_STAINED_GLASS_PANE, 1, true));
        }

        int CurrentLevel = BagStatus.get(0);
        int MaxLevel = 9;
        int DeltaLevel = MaxLevel - CurrentLevel;

        if(CurrentLevel == 0){
oreBagUpgradesInventory.setItem(13, ItemFactory.generateItemStack(oreBagUpgradesLockedName, Material.IRON_BARS, 1, true, Collections.singletonList("§bClick me to Unlock §cOre Bag §bfor " +
        ChatColor.GREEN + oreBagUpgradesLockedPrice + msg.cur)));
        }

        if (CurrentLevel >= 1) {
            oreBagUpgradesInventory.setItem(26, ItemFactory.generateItemStack(oreBagUpgradesResourcesName, Material.BUCKET, 1, true,
                    Collections.singletonList("§bClick me to view §cOre Bag§b Resources§6!")));
        }

        while (DeltaLevel > 0) {
            oreBagUpgradesInventory.setItem(MaxLevel - DeltaLevel, upgrades.get(MaxLevel - DeltaLevel).getLocked());
            DeltaLevel--;
        }


        while (CurrentLevel > 0) {
            oreBagUpgradesInventory.setItem(CurrentLevel - 1, upgrades.get(CurrentLevel-1).getUnlocked());
            CurrentLevel--;
        }

        return oreBagUpgradesInventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        List<Integer> BagStatus = HashmapBags.getBagMap().get(p.getUniqueId());
        if (e.getView().getTitle().equals(oreBagUpgradesInventoryName)) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.IRON_BARS) {
                if (BagStatus.get(0) >= 1) {
                    p.closeInventory();
                    p.sendMessage("§cOre Bag §balready Unlocked§6!");
                } else {
                    if (sql.getPoints(p.getUniqueId(), "CurrencyTable", "Geisten") >= oreBagUpgradesLockedPrice) {
                        sql.addPoints(p.getUniqueId(), oreBagUpgradesLockedPrice * -1, "CurrencyTable", "Geisten");
                        HashmapBags.updateBagMap(p, 1, 0);
                        p.closeInventory();
                        p.sendMessage("§bYou have unlocked §cOre Bag §bLevel §a1§6!");
                        p.openInventory(OreBagUpgradesMenu.oreBagUpgradesMenu(p));
                    } else {
                        p.sendMessage("§bInsufficient Balance to cover " + ChatColor.GREEN + oreBagUpgradesLockedPrice + msg.cur);
                        p.sendMessage("§bCurrent balance§6: " + ChatColor.GREEN + sql.getPoints(p.getUniqueId(), "CurrencyTable", "Geisten") + msg.cur);
                    }
                }
            }
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BUCKET) {
                p.openInventory(OreBagResourcesMenu.oreBagInv(p));
                }
            }
        }
    }

class OreBagUpgrades {

    String name;
    String unlockedName;
    String lockedName;

    Material material;
    List<String> lore;

    ItemStack unlocked;
    ItemStack locked;


    public OreBagUpgrades(Material material, String name, List<String> lore) {
        this.material = material;
        this.name = name;
        this.lore = lore;

        this.lockedName = name + " §7[§4Locked§7]";
        this.unlockedName = name + " §7[§2UnLocked§7]";

        createItems();
    }

    public void createItems() {
        this.unlocked = ItemFactory.generateItemStack(unlockedName, material, 1, true, lore);
        this.locked = ItemFactory.generateItemStack(lockedName, material, 1, false, lore);
    }

    public ItemStack getUnlocked() {
        return unlocked;
    }

    public ItemStack getLocked() {
        return locked;
    }

}
