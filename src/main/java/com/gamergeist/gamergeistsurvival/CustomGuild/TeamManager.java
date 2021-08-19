package com.gamergeist.gamergeistsurvival.CustomGuild;

import com.gamergeist.gamergeistsurvival.Files.ConfigManager;
import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import com.gamergeist.gamergeistsurvival.Messages.Message;
import com.gamergeist.gamergeistsurvival.Messages.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private HashMap<String, Team> teams = new HashMap<>();
    private HashMap<Player, Team> teams2 = new HashMap<>();

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
        Team tempTeam = new Team(TeamName, startingSize, permissions,
                TeamMembersOnline, TeamMembersOffline, priority);
        tempTeam.addLeader(player);
        teams.put(TeamName, tempTeam);
        teams2.put(player, tempTeam);
    }

    public Team getTeam(String TeamName) {
        if (teams.containsKey(TeamName))
            return teams.get(TeamName);
        return null;
    }

    public Team getTeam(Player player) {
        if (teams2.containsKey(player))
            return teams2.get(player);
        return null;
    }

    public void listTeam(Team team, Player player) {
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

    public static class Team {
        //TODO: teamSize while invite
        private static HashMap<String, List<Player>> TeamMembersOnline;
        private static HashMap<String, List<OfflinePlayer>> TeamMembersOffline;
        private static HashMap<String, HashMap<String, Boolean>> permissions;
        private static HashMap<String, Integer> priority;
        private static String TeamName;
        private int TeamSize;
        private int MinRankPriority;
        private int MaxRankPriority;
        private static MessageManager m;
        private static Message msg = Message.getInstance();

        public Team(String TeamName, int TeamSize, HashMap<String, HashMap<String, Boolean>> permissions,
                    HashMap<String, List<Player>> TeamMembersOnline, HashMap<String, List<OfflinePlayer>> TeamMembersOffline,
                    HashMap<String, Integer> priority) {
            this.TeamName = TeamName;
            this.permissions = permissions;
            this.TeamSize = TeamSize;
            this.TeamMembersOnline = TeamMembersOnline;
            this.TeamMembersOffline = TeamMembersOffline;
            this.priority = priority;

            setup();
        }

        public static void SetPermissions(HashMap<String,HashMap<String, Boolean>> perms){
                permissions = perms;
        }

        public static void SetTeamMembersOnline(HashMap<String,List<Player>> members){
            TeamMembersOnline = members;
        }

        public static void SetTeamMembersOffline(HashMap<String,List<OfflinePlayer>> members){
            TeamMembersOffline = members;
        }

        public void shiftPlayerOnline(Player player){
            TeamMembersOnline.get(getRole(player)).add(player);
            TeamMembersOffline.get(getRole(player)).removeIf(p -> (p.getUniqueId().equals(player.getUniqueId())));
        }

        public void shiftPlayerOffline(Player player){
            TeamMembersOnline.get(getRole(player)).remove(player);
            TeamMembersOffline.get(getRole(player)).add(Bukkit.getOfflinePlayer(player.getUniqueId()));
        }

        public void addLeader(Player player) {
            TeamMembersOnline.get(getRole(MaxRankPriority)).add(player);
            m.SendMessage("&cTeam: &b" + TeamName + " &cCreated", player);
        }

        public static void list(Player player) {
            StringBuilder sb = new StringBuilder(1000);
            sb.append("&6-----------------------------------------------------");
            sb.append("&4TeamMembers\n");
            TeamMembersOnline.keySet().forEach(role -> {
                sb.append("&1" + role + ": ");
                TeamMembersOnline.get(role).forEach(p -> sb.append("&a" + p.getName() + " "));
                TeamMembersOffline.get(role).forEach(p -> sb.append("&c" + p.getName() + " "));
                sb.append("\n");
            });
            sb.append("&6-----------------------------------------------------");
            m.SendMessage(sb.toString(), player);
        }


        public void setup() {
            this.MaxRankPriority = priority.values().stream().toList().get(0);
            this.MinRankPriority = priority.values().stream().toList().get(0);

            priority.values().forEach(i -> {

                if (MaxRankPriority > i) {
                    this.MaxRankPriority = i;
                } else if (MinRankPriority < i) {
                    this.MinRankPriority = i;
                }
            });

        }

        public boolean isinTeam(Player player) {
            for (List<Player> l : TeamMembersOnline.values()) {
                if (l.contains(player)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isinTeam(OfflinePlayer player) {
            for (List<OfflinePlayer> l : TeamMembersOffline.values()) {
                if (l.contains(player)) {
                    return true;
                }
            }
            return false;
        }

        public void addPlayer(Player player, String role) {
            if (!TeamMembersOnline.get(role).contains(player)) {
                TeamMembersOnline.get(role).add(player);
            }
        }

        public void addPlayer(OfflinePlayer player, String role) {
            if (!TeamMembersOffline.get(role).contains(player)) {
                TeamMembersOffline.get(role).add(player);
            }
        }


        public void removePlayer(Player player, String role) {
            if (TeamMembersOnline.get(role).contains(player)) {
                TeamMembersOnline.get(role).remove(player);
            }
        }

        public void removePlayer(OfflinePlayer player, String role) {
            if (TeamMembersOffline.get(role).contains(player)) {
                TeamMembersOffline.get(role).remove(player);
            }
        }

        public String getRole(Player player) {
            for (Map.Entry<String, List<Player>> entry : TeamMembersOnline.entrySet()) {
                if (entry.getValue().contains(player)) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public String getRole(OfflinePlayer player) {
            for (Map.Entry<String, List<OfflinePlayer>> entry : TeamMembersOffline.entrySet()) {
                if (entry.getValue().contains(player)) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public String getRole(int pri) {
            for (Map.Entry<String, Integer> entry : priority.entrySet()) {
                if (entry.getValue().intValue() == pri) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public int getPriority(String Role) {
            return priority.get(Role);
        }

        public int getPriority(Player player) {
            return priority.get(getRole(player));
        }

        public int getPriority(OfflinePlayer player) {
            return priority.get(getRole(player));
        }

        public void promote(Player player, Player sender) {
            if (getPermission(getRole(player), "Promote")) {
                if (isinTeam(player)) {
                    if (getPriority(player) != MaxRankPriority) {
                        addPlayer(player, getRole(getPriority(player) - 1));
                        removePlayer(player, getRole(player));
                        m.SendMessage(msg.playerPromoted, sender);
                        m.SendMessage(msg.playerPromoteMessage, player);
                        return;
                    }
                    m.SendMessage(msg.playerHighestPromotion, sender);
                    return;
                }
                m.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            m.SendMessage(msg.noPermission, sender);
        }

        public void promote(OfflinePlayer player, Player sender) {
            if (getPermission(getRole(player), "Promote")) {
                if (isinTeam(player)) {
                    if (getPriority(player) != MaxRankPriority) {
                        addPlayer(player, getRole(getPriority(player) - 1));
                        removePlayer(player, getRole(player));
                        m.SendMessage(msg.playerPromoted, sender);
                        //m.SendMessage(msg.playerPromoteMessage, player); //TODO: OnJoin Message
                        return;
                    }
                    m.SendMessage(msg.playerHighestPromotion, sender);
                    return;
                }
                m.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            m.SendMessage(msg.noPermission, sender);
        }

        public void demote(Player player, Player sender) {
            //TODO: add reason for demote kick and promote
            if (getPermission(getRole(player), "Demote")) {
                if (isinTeam(player)) {
                    if (getPriority(player) != MinRankPriority) {
                        addPlayer(player, getRole(getPriority(player) + 1));
                        removePlayer(player, getRole(player));
                        m.SendMessage(msg.playerDemoted, sender);
                        m.SendMessage(msg.playerDemoteMessage, player);
                        return;
                    }
                    m.SendMessage(msg.playerLowestDemotion, sender);
                    return;
                }
                m.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            m.SendMessage(msg.noPermission, sender);
        }

        public void demote(OfflinePlayer player, Player sender) {
            //TODO: add reason for demote kick and promote
            if (getPermission(getRole(player), "Demote")) {
                if (isinTeam(player)) {
                    if (getPriority(player) != MinRankPriority) {
                        addPlayer(player, getRole(getPriority(player) + 1));
                        removePlayer(player, getRole(player));
                        m.SendMessage(msg.playerDemoted, sender);
                        //m.SendMessage(msg.playerDemoteMessage, player); //TODO:onjoin message
                        return;
                    }
                    m.SendMessage(msg.playerLowestDemotion, sender);
                    return;
                }
                m.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            m.SendMessage(msg.noPermission, sender);
        }

        public void kick(Player player, Player sender) {
            if (getPermission(getRole(sender), "Kick")) {
                if (isinTeam(player)) {
                    removePlayer(player, getRole(player));
                    m.SendMessage(msg.playerKicked, sender);
                    m.SendMessage(msg.playerKickMessage, player);
                    return;
                }
                m.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            m.SendMessage(msg.noPermission, sender);
        }

        public void kick(OfflinePlayer player, Player sender) {
            if (getPermission(getRole(sender), "Kick")) {
                if (isinTeam(player)) {
                    removePlayer(player, getRole(player));
                    m.SendMessage(msg.playerKicked, sender);
                    //m.SendMessage(msg.playerKickMessage, player); TODO:on join message
                    return;
                }
                m.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            m.SendMessage(msg.noPermission, sender);
        }

        public void invite(Player player, Player sender) {
            if (getPermission(getRole(sender), "Invite")) {
                if (isinTeam(player)) {
                    addPlayer(player, getRole(MinRankPriority));
                    m.SendMessage(msg.playerInvited, sender);
                    m.SendMessage(msg.playerInvite, player);
                    return;
                }
                m.SendMessage(msg.playerAlreadyInYourTeam, sender);
                return;
            }
            m.SendMessage(msg.noPermission, sender);
        }

        public boolean getPermission(String role, String permission) {
            return permissions.get(role).get(permission);
        }

        public void setPermission(String role, String permission, boolean state) {
            permissions.get(role).replace(permission, state);
        }

        public void invertPermissions(String role, String permission) {
            setPermission(role, permission, !getPermission(role, permission));
        }
    }
}
/*


 */