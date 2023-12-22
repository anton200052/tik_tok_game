package me.vasylkov.tiktokgame.commands;

import me.vasylkov.tiktokgame.utils.GameProcessUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopGameCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("Только в игре!");
            return true;
        }

        Player player = (Player) sender;

        GameProcessUtils.stopGame();
        return true;
    }
}
