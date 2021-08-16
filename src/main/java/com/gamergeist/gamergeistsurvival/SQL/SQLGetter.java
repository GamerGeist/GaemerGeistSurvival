package com.gamergeist.gamergeistsurvival.SQL;

import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class SQLGetter {
    private final GamerGeistSurvival plugin;
    private static SQLGetter instance;

    public static SQLGetter getInstance() {
        return instance;
    }

    public SQLGetter(GamerGeistSurvival plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public void createPlayer(Player p, String table) {
        try {
            UUID uuid = p.getUniqueId();
            if (exists(uuid, table)) {
                PreparedStatement ps = plugin.sql.getConnection().prepareStatement("INSERT IGNORE INTO " + table + " (player,UUID) VALUES (?,?)");
                ps.setString(1, p.getName());
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean exists(UUID uuid, String table) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            return !results.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public void createOreBagTable() {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(
                    "Create table if not exists OreBagTable (Player Varchar(100),UUID Varchar(50)," +
                            "Coal int Default 0,Iron int Default 0,Gold int Default 0,Redstone int Default 0, Emerald int Default 0," +
                            "Diamond int Default 0,Ancient_debris int Default 0,Lapis int Default 0,Copper int Default 0," +
                            "Primary key(UUID))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createFarmBagTable() {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(
                    "Create table if not exists FarmBagTable (Player Varchar(100),UUID Varchar(50)," +
                            "berries int default 0," +
                            "apple int default 0," +
                            "wheat int default 0," +
                            "oak_sapling int default 0," +
                            "spruce_sapling int default 0," +
                            "birch_sapling int default 0," +
                            "jungle_sapling int default 0," +
                            "acacia_sapling int default 0," +
                            "dark_oak_sapling int default 0," +
                            "carrot int default 0," +
                            "potato int default 0," +
                            "beetroot int default 0," +
                            "cocoa_seed int default 0," +
                            "wheat_seed int default 0," +
                            "pumpkin_seed int default 0," +
                            "melon_seed int default 0," +
                            "beetroot_seed int default 0," +
                            "melon int default 0," +
                            "bread int default 0," +
                            "raw_pork int default 0," +
                            "raw_mutton int default 0," +
                            "raw_beef int default 0," +
                            "raw_chicken int default 0," +
                            "raw_salmon int default 0," +
                            "raw_cod int default 0," +
                            "tropical_fish int default 0," +
                            "pumpkin int default 0," +
                            "Primary key(UUID))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createWoodBagTable() {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(
                    "Create table if not exists WoodBagTable (Player Varchar(100),UUID Varchar(50)," +
                            "oak_plank int default 0," +
                            "spruce_plank int default 0," +
                            "birch_plank int default 0," +
                            "jungle_plank int default 0," +
                            "acacia_plank int default 0," +
                            "dark_oak_plank int default 0," +
                            "crimson_plank int default 0," +
                            "warped_plank int default 0," +
                            "oak_log int default 0," +
                            "spruce_log int default 0," +
                            "birch_log int default 0," +
                            "jungle_log int default 0," +
                            "acacia_log int default 0," +
                            "dark_oak_log int default 0," +
                            "crimson_stem int default 0," +
                            "warped_stem int default 0," +
                            "Primary key(UUID))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createCurrencyTable() {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(
                    "Create table if not exists CurrencyTable (Player Varchar(100),UUID Varchar(50)," +
                            "Geisten int Default 0, Gems int Default 0," +
                            "Primary key(UUID))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createBagUnlockTable() {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(
                    "Create table if not exists BagUnlockTable (Player Varchar(100),UUID Varchar(50),OreBag Int default 0," +
                            "WoodBag Int default 0,FarmBag Int default 0,Primary key(UUID))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createTeamTable(){
        try{
            StringBuilder tableCreate = new StringBuilder(500);
            tableCreate.append("Create table if not exists TeamTable (TeamName Varchar(50),serializedRoles MediumText,");
            tableCreate.append("serializedPermissions MediumText,primary key(TeamName)");
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(tableCreate.toString());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addPoints(UUID uuid, int points, String table, String parameter) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE " + table + " SET " + parameter + " =? WHERE UUID=?");
            ps.setInt(1, Integer.parseInt(String.valueOf((getPoints(uuid, table, parameter) + points))));
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getPoints(UUID uuid, String table, String parameter) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            int points;
            if (resultSet.next()) {
                points = resultSet.getInt(parameter);
                return points;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public List<Integer> getLVLs(UUID uuid, String table, String parameter1, String parameter2, String parameter3) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                List<Integer> list = new ArrayList<>();
                list.add(resultSet.getInt(parameter1));
                list.add(resultSet.getInt(parameter2));
                list.add(resultSet.getInt(parameter3));
                return list;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(0);
        list.add(0);
        return list;

    }

    public void updateBagMap(UUID uuid, HashMap<String, Integer> map, String table) {

        map.forEach((key, value) -> {
            try {
                PreparedStatement ps = plugin.sql.getConnection().prepareStatement("update " + table + " Set " + key + " = ? WHERE UUID=?");
                ps.setInt(1, value);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void CreateTeam(String teamName,String serializedPermissions,String serializedRoles){
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(
            "insert ignore into teamtable(teamname,serializedRoles,serializedPermissions) values(?,?,?) ");
            ps.setString(0,teamName);
            ps.setString(1,serializedRoles);
            ps.setString(2,serializedPermissions);
            ps.executeUpdate();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
