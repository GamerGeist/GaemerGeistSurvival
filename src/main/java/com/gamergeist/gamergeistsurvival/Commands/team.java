package com.gamergeist.gamergeistsurvival.Commands;

import com.gamergeist.gamergeistsurvival.CustomGuild.TeamManager;
import com.gamergeist.gamergeistsurvival.Files.ConfigManager;
import com.gamergeist.gamergeistsurvival.GUI.Team.Team;
import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import com.gamergeist.gamergeistsurvival.Messages.Message;
import com.gamergeist.gamergeistsurvival.Messages.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class team implements CommandExecutor {

    private static GamerGeistSurvival plugin;
    private static ConfigManager configs;
    private static team instance;

    private static FileConfiguration teamConfig;

    private static String team ;
    private Message msg;
    private MessageManager m;
    private TeamManager teams;

    public team(GamerGeistSurvival plugin) {
        this.plugin = plugin;
        this.instance = this;
        this.configs = ConfigManager.getinstance();
        this.msg = Message.getInstance();

        this.teamConfig = configs.getConfig("TeamSettings.yml").getConfig();
        this.teams = TeamManager.getInstance();
        team = "§5Guild";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (args.length < 1) {
                p.openInventory(Team.teamMenu(p));
                m.SendMessage(msg.teamMsgPrefix + " &bFor more help please refer to &c/guild help", p);
                return true;
            }
                if (args[0].equalsIgnoreCase("help")) {
                    m.SendMessage(
                            "&6-----------------------------------------------------" +
                                    msg.teamMsgPrefix + " &bWelcome to the help section of Guild&6.\n" +
                                    msg.teamMsgPrefix + "&bTo create a Guild, please type &c/guild create\n" +
                                    msg.teamMsgPrefix + "&bTo enable chat, please type &c/guild chat\n" +
                                    msg.teamMsgPrefix + "&bTo claim land, please type &c/guild claim\n" +
                                    "&6-----------------------------------------------------", p);
                    return true;
                }
                else if (args[0].equalsIgnoreCase("info")) {
                    if (args.length < 2) {
                        m.SendMessage(msg.teamMsgPrefix + " Please provide the Guild name", p);
                        return true;
                    }
                    if (args.length >= 3) {
                        m.SendMessage(
                                "&6-----------------------------------------------------" +
                                        msg.teamMsgPrefix + " §bGuild name§6: \n" +
                                        msg.teamMsgPrefix + "&bLeader§6:\n" +
                                        msg.teamMsgPrefix + "&bGuild Members§6:\n" +
                                        "&6-----------------------------------------------------", p);
                        return true;
                    }
                }
                else if (args[0].equalsIgnoreCase("list")) {
                    teams.listTeam(teams.getTeam(p),p);
                    return true;
                }
                // Creating the Team
                else if (args[0].equalsIgnoreCase("create")) {
                    if (args.length < 2) {
                        m.SendMessage(msg.teamMsgPrefix + " " + msg.incorrectFormat +
                                " &bPlease use &c/guild create {Guild Name} ", p);
                        return true;
                    }
                    //Create StringBuilder for team name in case they want to add Spaces.
                    StringBuilder sb = new StringBuilder(30);
                    for(int i = 1;i<args.length;i++) {
                        sb.append(args[i]);
                        if(args.length-1 != i){
                            sb.append(" ");
                        }
                    }
                    teams.createTeam(p,sb.toString());
                    return true;


                /* Team Chat
/*                if (args[0].equalsIgnoreCase("chat")) {
                    if (args.length < 2) {
                        m.SendMessage(msg.teamMsgPrefix + " " + msg.incorrectFormat + " &bPlease use &c/" + teamCommand +
                                " chat on|off", p);
                    }
                    if (args[1].equalsIgnoreCase("on")) {
                        if (!Chat == Enabled) {
                            //Enable team chat
                            m.SendMessage(msg.teamMsgPrefix + " " + msg.teamChatEnabled, p);
                        } else {
                            m.SendMessage(msg.teamMsgPrefix + " " + msg.teamChatAlreadyEnabled, p);
                        }
                    }
                    if (args[1].equalsIgnoreCase("off")) {
                        if (Chat == Enabled) {
                            Disable team chat
                            m.SendMessage(msg.teamMsgPrefix + " " + msg.teamChatDisabled, p);
                        } else {
                            m.SendMessage(msg.teamMsgPrefix + " " + msg.teamChatAlreadyDisabled, p);
                        }
                    }
                } */
            }
              m.SendMessage(msg.teamMsgPrefix + " Unknown argument! " + "&bplease refer to &c/guild help", p);
              return true;
            }
        else {
            m.ConsoleMessage(msg.teamMsgPrefix + " &4You must be a Player to execute this command");
            return true;
        }


    }
}
