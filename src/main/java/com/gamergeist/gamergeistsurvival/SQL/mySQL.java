package com.gamergeist.gamergeistsurvival.SQL;

import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class mySQL {

    private final GamerGeistSurvival plugin;
    public mySQL(GamerGeistSurvival plugin){this.plugin =plugin;}

    private Connection connection = null;
    
    public boolean isConnected() {
        return (connection != null);
    }
    public void connect() throws ClassNotFoundException, SQLException {
        if(!isConnected()) {
            String username = plugin.getConfig().getString("SQL.Username");
            String password = plugin.getConfig().getString("SQL.Password");
            String host = plugin.getConfig().getString("SQL.Host");
            String database = plugin.getConfig().getString("SQL.Database");
            connection = DriverManager.getConnection("jdbc:mysql://"+host+"/"+database+"?allowPublicKeyRetrieval=true&useSSL=false", username, password);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}