package com.gamergeist.gamergeistsurvival.SQL.HashMaps;

import com.gamergeist.gamergeistsurvival.CustomGuild.Teams;
import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import com.gamergeist.gamergeistsurvival.SQL.SQLGetter;
import com.gamergeist.gamergeistsurvival.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class HashmapTeams implements Listener {

    private static final HashMap<String, Teams>TeamsHashMap = new HashMap<>();
    private GamerGeistSurvival plugin;
    private static SQLGetter sql;

    public HashmapTeams(GamerGeistSurvival plugin) {
    this.plugin = plugin;
    this.sql = SQLGetter.getInstance();
    }

    public static void AddTeam(String TeamName) {
        int TeamSize = sql.getTeamInt(TeamName,"teamSize");
        Pair<HashMap<String, List<Player>>,HashMap<String, List<OfflinePlayer>>> p = getSqlMembers(TeamName);
        TeamsHashMap.putIfAbsent(TeamName,new Teams(TeamName,TeamSize,getSqlPerms(TeamName),p.first(),p.second()));
    }

    public static void RemoveTeam(String TeamName){
        if(TeamsHashMap.containsKey(TeamName)){
            TeamsHashMap.remove(TeamName);
            }
    }


    public static HashMap<String,HashMap<String,Boolean>> getSqlPerms(String TeamName){
            String perm = sql.getTeam(TeamName, "serializedPermissions");
            HashMap<String,HashMap<String,Boolean>> permissions = new HashMap<>();
            String[] p = perm.split("|");
            for (int i = 0; i < p.length; i++) {
                String role = p[i].split("-")[0];
                String perms = p[i].split("-")[1];
                HashMap<String,Boolean> tempperm = new HashMap<>();
                for(String a :perms.split(",")){
                    tempperm.putIfAbsent(a.split(":")[0],Boolean.parseBoolean(a.split(":")[1]));
                }
                permissions.putIfAbsent(role,tempperm);
            }
        return permissions;
    }

    public static Pair<HashMap<String, List<Player>>,
    HashMap<String, List<OfflinePlayer>>> getSqlMembers(String TeamName){
       HashMap<String, List<Player>> onlinePlayers = new HashMap<>();
       HashMap<String, List<OfflinePlayer>> offlinePlayers = new HashMap<>();
       String roles = sql.getTeam(TeamName, "serializedRoles");
       String[] r = roles.split("|");
       for (int i = 0; i < r.length; i++) {
            String role = r[i].split("-")[0];
            String[] Players = r[i].split("-")[1].split(",");
            for (int j = 0; j < Players.length; j++) {
                List<Player> p1 = new ArrayList<>();
                List<OfflinePlayer> p2 = new ArrayList<>();
                OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(Players[j]));
                if(p.isOnline()){
                    p1.add(p.getPlayer());
                }else{
                    p2.add(p);
                }
                onlinePlayers.putIfAbsent(role,p1);
                offlinePlayers.putIfAbsent(role,p2);
            }
       }
       return new Pair<HashMap<String, List<Player>>,HashMap<String, List<OfflinePlayer>>>(onlinePlayers,offlinePlayers);
    }


    public static String getPermissionsString(String TeamName){
        Teams team = TeamsHashMap.get(TeamName);
        StringBuilder sb = new StringBuilder(1000);
        team.getPermissions().entrySet().forEach(entry->{
            sb.append(entry.getKey()+"-");
            entry.getValue().entrySet().forEach(entry2->{
                sb.append(entry2.getKey()+":"+entry2.getValue()+",");
            });
            sb.setCharAt(sb.lastIndexOf(","),'|');
        });
        sb.deleteCharAt(sb.lastIndexOf("|"));
        return sb.toString();
    }

    public static String getMembersString(String TeamName){
           Teams team = TeamsHashMap.get(TeamName);
           StringBuilder sb = new StringBuilder(1000);
           team.getMembers().first().entrySet().forEach(entry->{
                String role = entry.getKey();
                sb.append(role).append("-");
                entry.getValue().forEach(p -> sb.append(p+","));
                team.getMembers().second().get(role).forEach(p -> sb.append(p+","));
                sb.setCharAt(sb.lastIndexOf(","),'|');

           });
            sb.deleteCharAt(sb.lastIndexOf("|"));
            return sb.toString();
    }

    public void SaveSql(){
          TeamsHashMap.keySet().forEach(teamname ->
            sql.updateTeamTable(teamname,getPermissionsString(teamname),
            getMembersString(teamname),TeamsHashMap.get(teamname).getTeamSize()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String teamName = sql.getTeamName(p.getUniqueId().toString());
        if(teamName != null){
            if(!TeamsHashMap.containsKey(teamName)){
            AddTeam(teamName);
            }
            TeamsHashMap.get(teamName).shiftPlayerOnline(p);
        }
    }
}

