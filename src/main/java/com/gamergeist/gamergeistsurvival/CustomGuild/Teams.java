package com.gamergeist.gamergeistsurvival.CustomGuild;

import com.gamergeist.gamergeistsurvival.Files.ConfigManager;
import com.gamergeist.gamergeistsurvival.Messages.Message;
import com.gamergeist.gamergeistsurvival.Messages.MessageManager;
import com.gamergeist.gamergeistsurvival.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teams {
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

        public Teams(String TeamName, int TeamSize, HashMap<String, HashMap<String, Boolean>> permissions,
                     HashMap<String, List<Player>> TeamMembersOnline, HashMap<String, List<OfflinePlayer>> TeamMembersOffline) {
            this.TeamName = TeamName;
            this.permissions = permissions;
            this.TeamSize = TeamSize;
            this.TeamMembersOnline = TeamMembersOnline;
            this.TeamMembersOffline = TeamMembersOffline;

            setPriority();
            setup();
        }

        public void setPriority(){
         ConfigManager.getinstance().getConfig("TeamSettings.yml").getConfig().getConfigurationSection("TeamRoles").getKeys(false).forEach(role ->
          priority.putIfAbsent(role,ConfigManager.getinstance().getConfig("TeamSettings.yml").getConfig().getInt("TeamRoles." + role + ".Priority")));
        }

        public static void SetPermissions(HashMap<String,HashMap<String, Boolean>> perms){
                permissions = perms;
        }

        public HashMap<String,HashMap<String,Boolean>> getPermissions(){
            return permissions;
        }
        
        public int getTeamSize(){
            return TeamSize;
        }

        public Pair<HashMap<String, List<Player>>,HashMap<String, List<OfflinePlayer>>> getMembers(){
            return new Pair<HashMap<String, List<Player>>,HashMap<String, List<OfflinePlayer>>>(TeamMembersOnline,TeamMembersOffline);
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
            MessageManager.SendMessage("&cTeam: &b" + TeamName + " &cCreated", player);
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
            MessageManager.SendMessage(sb.toString(), player);
        }


        public void setup() {
            this.MaxRankPriority = Integer.MAX_VALUE;
            this.MinRankPriority = Integer.MIN_VALUE;

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
                        MessageManager.SendMessage(msg.playerPromoted, sender);
                        MessageManager.SendMessage(msg.playerPromoteMessage, player);
                        return;
                    }
                    MessageManager.SendMessage(msg.playerHighestPromotion, sender);
                    return;
                }
                MessageManager.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            MessageManager.SendMessage(msg.noPermission, sender);
        }

        public void promote(OfflinePlayer player, Player sender) {
            if (getPermission(getRole(player), "Promote")) {
                if (isinTeam(player)) {
                    if (getPriority(player) != MaxRankPriority) {
                        addPlayer(player, getRole(getPriority(player) - 1));
                        removePlayer(player, getRole(player));
                        MessageManager.SendMessage(msg.playerPromoted, sender);
                        //m.SendMessage(msg.playerPromoteMessage, player); //TODO: OnJoin Message
                        return;
                    }
                    MessageManager.SendMessage(msg.playerHighestPromotion, sender);
                    return;
                }
                MessageManager.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            MessageManager.SendMessage(msg.noPermission, sender);
        }

        public void demote(Player player, Player sender) {
            //TODO: add reason for demote kick and promote
            if (getPermission(getRole(player), "Demote")) {
                if (isinTeam(player)) {
                    if (getPriority(player) != MinRankPriority) {
                        addPlayer(player, getRole(getPriority(player) + 1));
                        removePlayer(player, getRole(player));
                        MessageManager.SendMessage(msg.playerDemoted, sender);
                        MessageManager.SendMessage(msg.playerDemoteMessage, player);
                        return;
                    }
                    MessageManager.SendMessage(msg.playerLowestDemotion, sender);
                    return;
                }
                MessageManager.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            MessageManager.SendMessage(msg.noPermission, sender);
        }

        public void demote(OfflinePlayer player, Player sender) {
            //TODO: add reason for demote kick and promote
            if (getPermission(getRole(player), "Demote")) {
                if (isinTeam(player)) {
                    if (getPriority(player) != MinRankPriority) {
                        addPlayer(player, getRole(getPriority(player) + 1));
                        removePlayer(player, getRole(player));
                        MessageManager.SendMessage(msg.playerDemoted, sender);
                        //m.SendMessage(msg.playerDemoteMessage, player); //TODO:onjoin message
                        return;
                    }
                    MessageManager.SendMessage(msg.playerLowestDemotion, sender);
                    return;
                }
                MessageManager.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            MessageManager.SendMessage(msg.noPermission, sender);
        }

        public void kick(Player player, Player sender) {
            if (getPermission(getRole(sender), "Kick")) {
                if (isinTeam(player)) {
                    removePlayer(player, getRole(player));
                    MessageManager.SendMessage(msg.playerKicked, sender);
                    MessageManager.SendMessage(msg.playerKickMessage, player);
                    return;
                }
                MessageManager.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            MessageManager.SendMessage(msg.noPermission, sender);
        }

        public void kick(OfflinePlayer player, Player sender) {
            if (getPermission(getRole(sender), "Kick")) {
                if (isinTeam(player)) {
                    removePlayer(player, getRole(player));
                    MessageManager.SendMessage(msg.playerKicked, sender);
                    //m.SendMessage(msg.playerKickMessage, player); TODO:on join message
                    return;
                }
                MessageManager.SendMessage(msg.playerNotInTeam, sender);
                return;
            }
            MessageManager.SendMessage(msg.noPermission, sender);
        }

        public void invite(Player player, Player sender) {
            if (getPermission(getRole(sender), "Invite")) {
                if (isinTeam(player)) {
                    addPlayer(player, getRole(MinRankPriority));
                    MessageManager.SendMessage(msg.playerInvited, sender);
                    MessageManager.SendMessage(msg.playerInvite, player);
                    return;
                }
                MessageManager.SendMessage(msg.playerAlreadyInYourTeam, sender);
                return;
            }
            MessageManager.SendMessage(msg.noPermission, sender);
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

