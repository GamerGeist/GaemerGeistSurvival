package com.gamergeist.gamergeistsurvival;

import com.gamergeist.gamergeistsurvival.Commands.Balance;
import com.gamergeist.gamergeistsurvival.Commands.bags;
import com.gamergeist.gamergeistsurvival.Commands.team;
import com.gamergeist.gamergeistsurvival.CustomGuild.TeamManager;
import com.gamergeist.gamergeistsurvival.Files.ConfigManager;
import com.gamergeist.gamergeistsurvival.GUI.Bags.*;
import com.gamergeist.gamergeistsurvival.Messages.Message;
import com.gamergeist.gamergeistsurvival.Messages.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.gamergeist.gamergeistsurvival.SQL.*;
import java.sql.SQLException;
import java.util.Objects;



public final class GamerGeistSurvival extends JavaPlugin implements Listener {


    private static GamerGeistSurvival instance;

    public static GamerGeistSurvival getInstance() {
        return instance;
    }

    public mySQL sql;
    public static Message msg;
    public static SQLGetter data;
    public static TeamManager tman;
    public static ConfigManager config;
    public static team team;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = new ConfigManager(this);
        sql = new mySQL(this);
        data = new SQLGetter(this);
        msg = new Message(this);
        tman = new TeamManager(this);
        team = new team(this);


        connectToSql();
        registerCommands();
        registerListeners();
        HashMapCreation.AddExistingPlayers();
    }

    @Override
    public void onDisable(){
        Bukkit.getServer().getConsoleSender().sendMessage("§cPlugin Disabling");
        HashMapCreation.SaveExistingPlayers();
    }

    public void registerCommands() {
        register("balance", new Balance());
        register("bags", new bags());
        register("guild", new team(this));
    }

    public void registerListeners() {
        register(this);
        register(new HashMapCreation(this));
        register(new BagsMenu(this));
        register(new OreBagUpgradesMenu());
        register(new WoodBagUpgradesMenu());
        register(new FarmBagUpgradesMenu());
        register(new OreBagResourcesMenu());
    }

    public void connectToSql(){
        try {
            sql.connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            MessageManager.ConsoleMessage(msg.datanocon);
        }

        if (sql.isConnected()) {
            MessageManager.ConsoleMessage(msg.datacon);
            data.createOreBagTable();
            data.createWoodBagTable();
            data.createFarmBagTable();
            data.createCurrencyTable();
            data.createBagUnlockTable();
            data.createTeamTable();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (sql.isConnected()) {

            Bukkit.getServer().getConsoleSender().sendMessage("§aDatabase connected, Checking table for " + p.getName());

            if (data.exists(p.getUniqueId(), "OreBagTable")) {
                data.createPlayer(p, "OreBagTable");
            }
            if (data.exists(p.getUniqueId(), "WoodBagTable")) {
                data.createPlayer(p, "WoodBagTable");
            }
            if (data.exists(p.getUniqueId(), "FarmBagTable")) {
                data.createPlayer(p, "FarmBagTable");
            }
            if (data.exists(p.getUniqueId(), "CurrencyTable")) {
                data.createPlayer(p, "CurrencyTable");
            }
            if (data.exists(p.getUniqueId(), "BagUnlockTable")) {
                data.createPlayer(p, "BagUnlockTable");
            }
        }
        else {
            try {
                sql.connect();
            } catch (ClassNotFoundException | SQLException exception) {
                exception.printStackTrace();
                MessageManager.ConsoleMessage(msg.datanocon);
            }
        }

        e.setJoinMessage(null);
        String name = p.getName();
        String displayname = p.getDisplayName();

        String broadcastMessage = Objects.requireNonNull(this.getConfig().getString("broadcast-message.join-message-broadcast")).replaceAll("%player_name%", name).replaceAll("%player_display_name%", displayname);
        broadcastMessage = ChatColor.translateAlternateColorCodes('&', broadcastMessage);
        String privateMessage = Objects.requireNonNull(this.getConfig().getString("private-message.join-message-private")).replaceAll("%player_name%", name).replaceAll("%player_display_name%", displayname);
        privateMessage = ChatColor.translateAlternateColorCodes('&', privateMessage);

        if (this.getConfig().getBoolean("broadcast-player-join-message")) {
            for (Player target : Bukkit.getServer().getOnlinePlayers()) {
                target.sendMessage(broadcastMessage);
            }
        }
        if (this.getConfig().getBoolean("private-player-join-message")) {
            p.sendMessage(privateMessage);
        }

    }

    public void register(Listener l) {
        Bukkit.getPluginManager().registerEvents(l,this);
    }

    public void register(String n, CommandExecutor ce) {
        Objects.requireNonNull(getCommand(n)).setExecutor(ce);
    }
}
