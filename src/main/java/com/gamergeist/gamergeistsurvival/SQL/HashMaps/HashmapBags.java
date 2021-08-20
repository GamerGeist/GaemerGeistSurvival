package com.gamergeist.gamergeistsurvival.SQL.HashMaps;

import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import com.gamergeist.gamergeistsurvival.Messages.MessageManager;
import com.gamergeist.gamergeistsurvival.SQL.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HashmapBags implements Listener {

    private static final HashMap<UUID, List<Integer>> BagUnlocked = new HashMap<>();
    private GamerGeistSurvival plugin;
    private static SQLGetter sql;

    public HashmapBags(GamerGeistSurvival plugin) {
    this.plugin = plugin;
    this.sql = SQLGetter.getInstance();
    }

    public static void AddExistingPlayers(){
        Bukkit.getOnlinePlayers().forEach(HashmapBags::Add);
    }

    public static void SaveExistingPlayers(){
        Bukkit.getOnlinePlayers().forEach(p->{
                updateBagSQL(p);
                Remove(p);
        });
    }

    public static void Add(Player player) {
        UUID uuid = player.getUniqueId();
        List<Integer> bags = SQLGetter.getInstance().getLVLs(uuid, "BagUnlockTable", "OreBag", "WoodBag", "FarmBag");
        BagUnlocked.put(uuid, bags);
    }

    public static void Remove(Player player) {
        UUID uuid = player.getUniqueId();
        BagUnlocked.remove(uuid);
    }

    public static HashMap<UUID, List<Integer>> getBagMap() {
        return BagUnlocked;
    }

    public static void updateBagMap(Player player, int bag, int number) {  //0 for ore bag | 1 for wood bag | 2 for farm bag
        UUID uuid = player.getUniqueId();
        List<Integer> list = BagUnlocked.get(uuid);
        list.set(number, bag);
        BagUnlocked.replace(uuid, list);
    }

    public static void updateBagSQL(Player player) {
        UUID uuid = player.getUniqueId();
        List<Integer> list = BagUnlocked.get(uuid);
        HashMap<String,Integer> leaveSaveProgress = new HashMap<>();
        leaveSaveProgress.put("orebag",list.get(0));
        leaveSaveProgress.put("woodbag",list.get(1));
        leaveSaveProgress.put("farmbag",list.get(2));
        SQLGetter.getInstance().updateBagMap(uuid,leaveSaveProgress,"BagUnlockTable");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Add(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        MessageManager.ConsoleMessage("&aPlayer left, uploading contents to Database for &b" + e.getPlayer().getName());
        updateBagSQL(e.getPlayer());
        Remove(e.getPlayer());

        e.setQuitMessage(null);
        Player p = e.getPlayer();
        String name = p.getName();
        String displayname = p.getDisplayName();

        String broadcastMessage = Objects.requireNonNull(plugin.getConfig().getString("broadcast-message.quit-message-broadcast")).replaceAll("%player_name%", name).replaceAll("%player_display_name%", displayname);
        broadcastMessage = ChatColor.translateAlternateColorCodes('&', broadcastMessage);
        String privateMessage = Objects.requireNonNull(plugin.getConfig().getString("private-message.join-message-private")).replaceAll("%player_name%", name).replaceAll("%player_display_name%", displayname);
        privateMessage = ChatColor.translateAlternateColorCodes('&', privateMessage);

        if (GamerGeistSurvival.getInstance().getConfig().getBoolean("broadcast-player-quit-message")) {
            for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                target.sendMessage(broadcastMessage);
            }
        }
        if (GamerGeistSurvival.getInstance().getConfig().getBoolean("private-player-quit-message")) {
            p.sendMessage(privateMessage);
        }
    }
}