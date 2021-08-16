package com.gamergeist.gamergeistsurvival.Commands;

import com.gamergeist.gamergeistsurvival.GUI.Bags.BagsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class bags implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;

        p.openInventory(BagsMenu.bagsMenu(p));

        return false;
    }
}
