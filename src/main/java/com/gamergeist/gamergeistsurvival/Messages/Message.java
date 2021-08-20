package com.gamergeist.gamergeistsurvival.Messages;

import com.gamergeist.gamergeistsurvival.Files.ConfigManager;
import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Message {

    private final GamerGeistSurvival plugin;
    private final FileConfiguration config;
    private final FileConfiguration teamMessages;
    private final FileConfiguration teamConfig;
    public static Message instance;

    public String cur;
    public String cur2;
    public String datanocon;
    public String datacon;
    public String noperm;

    //Team Messages
    public String playerNotFound;
    public String teamMsgPrefix;
    public String createdTeam;
    public String noPermission;
    public String teamChatEnabled;
    public String teamChatDisabled;
    public String teamChatAlreadyEnabled;
    public String teamChatAlreadyDisabled;
    public String incorrectFormat;
    public String playerNotInTeam;
    public String playerAlreadyInATeam;
    public String playerAlreadyInYourTeam;
    public String playerInvited;
    public String playerInvite;
    public String playerAlreadyInvited;
    public String playerKicked;
    public String playerKickMessage;
    public String playerPromoted;
    public String playerPromoteMessage;
    public String playerHighestPromotion;
    public String playerDemoted;
    public String playerDemoteMessage;
    public String playerLowestDemotion;
    public String permissionChanged;

    public Message(GamerGeistSurvival plugin) {
        this.plugin = plugin;
        instance = this;
        config = plugin.getConfig();
        teamMessages = ConfigManager.getinstance().getConfig("messages.yml").getConfig();
        teamConfig = ConfigManager.getinstance().getConfig("TeamSettings.yml").getConfig();
        setMessages();
    }

    public static Message getInstance(){
        return instance;
    }

    public void setMessages() {
        cur = config.getString("Custom-Messages.main-currency-color");
        cur2 = config.getString("Custom-Messages.premium-currency-color") + Objects.requireNonNull(config.getString("Custom-Messages.premium-currency")).replaceAll("&", "§");
        datanocon = Objects.requireNonNull(config.getString("Custom-Messages.database-not-connected")).replaceAll("&", "§");
        datacon = Objects.requireNonNull(config.getString("Custom-Messages.database-connected")).replaceAll("&", "§");
        noperm = Objects.requireNonNull(config.getString("Custom-Messages.no-perm")).replaceAll("&", "§");

        //Team messages
        teamMsgPrefix = Objects.requireNonNull(teamConfig.getString("Prefix")).replaceAll("&", "§");
        playerNotFound = teamMessages.getString("Team.player-not-found");
        createdTeam = teamMessages.getString("Team.created-team");
        noPermission = teamMessages.getString("Team.no-permission");
        teamChatEnabled = teamMessages.getString("Team.team-chat-enabled");
        teamChatDisabled = teamMessages.getString("Team.team-chat-disabled");
        teamChatAlreadyEnabled = teamMessages.getString("Team.team-chat-already-enabled");
        teamChatAlreadyDisabled = teamMessages.getString("Team.team-chat-already-disabled");
        incorrectFormat = teamMessages.getString("Team.incorrect-format");
        playerNotInTeam = teamMessages.getString("Team.player-not-in-team");
        playerAlreadyInATeam = teamMessages.getString("Team.player-already-in-a-team");
        playerAlreadyInYourTeam = teamMessages.getString("Team.player-already-in-your-team");
        playerInvited = teamMessages.getString("Team.player-invited");
        playerInvite = teamMessages.getString("Team.player-invite");
        playerAlreadyInvited = teamMessages.getString("Team.player-already-invited");
        playerKicked = teamMessages.getString("Team.player-kicked");
        playerKickMessage = teamMessages.getString("Team.player-kick-message");
        playerPromoted = teamMessages.getString("Team.player-promoted");
        playerPromoteMessage = teamMessages.getString("Team.player-promote-message");
        playerHighestPromotion = teamMessages.getString("Team.player-highest-promotion");
        playerDemoted = teamMessages.getString("Team.player-demoted");
        playerDemoteMessage = teamMessages.getString("Team.player-demote-message");
        playerLowestDemotion = teamMessages.getString("Team.player-lowest-demotion");
        permissionChanged = teamMessages.getString("Team.permission-changed");
    }
}