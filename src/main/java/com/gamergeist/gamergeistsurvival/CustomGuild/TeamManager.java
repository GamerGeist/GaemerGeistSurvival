package com.gamergeist.gamergeistsurvival.CustomGuild;

import com.gamergeist.gamergeistsurvival.Files.ConfigManager;
import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamManager {

    private String teamPrefix;
    private String teamName;
    private int maxPlayersPerTeam;
    private String teamChatPrefix;
    private String noClaimCenter;
    private boolean teamSizeUpgradeable;
    private boolean claims;
    private boolean banks;
    private boolean teamChat;
    private int startingSize;
    private int baseClaimArea;
    private int areaPerPlayer;
    private int noClaimDistance;
    private int maxBankCapacity;

    private HashMap<String, HashMap<String, Boolean>> permissions = new HashMap<>();
    private HashMap<String, Integer> priority = new HashMap<>();
    private HashMap<String, List<Player>> TeamMembersOnline = new HashMap<>();
    private HashMap<String, List<OfflinePlayer>> TeamMembersOffline = new HashMap<>();
    private HashMap<String, Teams> teams = new HashMap<>();
    private HashMap<Player, Teams> teams2 = new HashMap<>();

    private static GamerGeistSurvival plugin;
    private static ConfigManager configs;
    private static TeamManager instance;

    private static FileConfiguration teamConfig;

    public TeamManager(GamerGeistSurvival plugin) {
        this.plugin = plugin;
        this.instance = this;
        this.configs = ConfigManager.getinstance();

        this.teamConfig = configs.getConfig("TeamSettings.yml").getConfig();
        initializePermissions();
        initializeTeams();
    }

    public static TeamManager getInstance() {
        return instance;
    }

    public void createTeam(Player player, String TeamName) {
        Teams tempTeam = new Teams(TeamName, startingSize, permissions,
                TeamMembersOnline, TeamMembersOffline);
        tempTeam.addLeader(player);
        teams.put(TeamName, tempTeam);
        teams2.put(player, tempTeam);
    }

    public Teams getTeam(String TeamName) {
        if (teams.containsKey(TeamName))
            return teams.get(TeamName);
        return null;
    }

    public Teams getTeam(Player player) {
        if (teams2.containsKey(player))
            return teams2.get(player);
        return null;
    }

    public void listTeam(Teams team, Player player) {
        team.list(player);
    }

    public void setStrings() {

        teamPrefix = teamConfig.getString("Prefix");
        teamName = teamConfig.getString("TeamName");
        teamChatPrefix = teamConfig.getString("TeamChat.TeamChatPrefix");
        noClaimCenter = teamConfig.getString("Claims.NoClaimZone.Center");
    }

    public void setInts() {
        maxPlayersPerTeam = teamConfig.getInt("TeamSize.Max-Players-Per-Team");
        startingSize = teamConfig.getInt("TeamSize.StartingSize");
        baseClaimArea = teamConfig.getInt("Claims.BaseClaimArea");
        areaPerPlayer = teamConfig.getInt("Claims.AreaPerPlayer");
        noClaimDistance = teamConfig.getInt("Claims.NoClaimZone.Distance");
        maxBankCapacity = teamConfig.getInt("Banks.MaxBankCap");
    }

    public void setBooleans() {
        teamSizeUpgradeable = teamConfig.getBoolean("TeamSize.Upgradeable");
        claims = teamConfig.getBoolean("Claims.Enabled");
        banks = teamConfig.getBoolean("Banks.Enabled");
        teamChat = teamConfig.getBoolean("TeamChat.Enabled");
    }

    private void initializePermissions() {

        teamConfig.getConfigurationSection("TeamRoles").getKeys(false).forEach(role -> {
            HashMap<String, Boolean> perm = new HashMap<String, Boolean>();
            priority.putIfAbsent(role, teamConfig.getInt("TeamRoles." + role + ".Priority"));
            teamConfig.getConfigurationSection("TeamRoles." + role).getKeys(false).forEach(permission -> {
                perm.putIfAbsent(permission, teamConfig.getBoolean("TeamRoles." + role + ".Permissions." + permission));
            });
            permissions.putIfAbsent(role, perm);
        });
    }

    private void initializeTeams() {
        teamConfig.getConfigurationSection("TeamRoles").getKeys(false).forEach(role -> {
            TeamMembersOnline.putIfAbsent(role, new ArrayList<Player>());
            TeamMembersOffline.putIfAbsent(role, new ArrayList<OfflinePlayer>());
        });

    }


}
/*


 */