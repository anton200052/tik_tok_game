package me.vasylkov.tiktokgame.commands;

import me.vasylkov.tiktokgame.listeners.BlockPlaceListener;
import me.vasylkov.tiktokgame.utils.TiktokConnectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndGameCommand implements CommandExecutor
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

        BlockPlaceListener.getCountdownTask().endGame();
        TiktokConnectionUtils.disconnect();

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSuccessfully end the game. Goodbye!"));
        return true;
    }
}
