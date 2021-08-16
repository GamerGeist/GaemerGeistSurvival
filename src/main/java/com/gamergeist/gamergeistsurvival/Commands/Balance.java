package com.gamergeist.gamergeistsurvival.Commands;

import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import com.gamergeist.gamergeistsurvival.Messages.MessageManager;
import com.gamergeist.gamergeistsurvival.SQL.SQLGetter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.gamergeist.gamergeistsurvival.GamerGeistSurvival.msg;

public class Balance implements CommandExecutor {
    private final SQLGetter sql;

    public Balance(){
        sql = SQLGetter.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if(args.length < 1){
                //deny message here
                MessageManager.SendMessage("&cSyntax: Either do /balance " +
                        GamerGeistSurvival.getInstance().getConfig().getString("Custom-Messages.main-currency") +
                        "or /balance " +
                        GamerGeistSurvival.getInstance().getConfig().getString("Custom-Messages.premium-currency") ,p);
                return true;
            }
            if(args[0].equalsIgnoreCase(GamerGeistSurvival.getInstance().getConfig().getString("Custom-Messages.main-currency"))){
                MessageManager.SendMessage("§bYour Balance is: " + ChatColor.GREEN + sql.getPoints(p.getUniqueId(), "CurrencyTable", "Geisten")+msg.cur, p);
                return true;
            }
            if(args[0].equalsIgnoreCase(GamerGeistSurvival.getInstance().getConfig().getString("Custom-Messages.premium-currency"))){
            MessageManager.SendMessage("§bYour Balance is: " + ChatColor.GREEN + sql.getPoints(p.getUniqueId(), "CurrencyTable", "Gems")+msg.cur2, p);
            return true;
            }
            MessageManager.SendMessage("&cSyntax: Either do /balance " +
                    GamerGeistSurvival.getInstance().getConfig().getString("Custom-Messages.main-currency") +
                    "or /balance " +
                    GamerGeistSurvival.getInstance().getConfig().getString("Custom-Messages.premium-currency") ,p);
            return true;
        }
        return true;
    }
}
