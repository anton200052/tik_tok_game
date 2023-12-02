package me.vasylkov.tiktokgame.commands;

import io.github.jwdeveloper.tiktok.TikTokLive;
import me.vasylkov.tiktokgame.TiktokGame;
import me.vasylkov.tiktokgame.listeners.BlockPlaceListener;
import me.vasylkov.tiktokgame.utils.TiktokConnectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartGameCommand implements CommandExecutor
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

        String hostName = TiktokGame.getInstance().getConfig().getString("hostName");

        if (!TikTokLive.isHostNameValid(hostName))
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Hostname: " + hostName + " is not valid!"));
            return false;
        }

        if (!TikTokLive.isLiveOnline(hostName))
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Hostname: " + hostName + " is not online!"));
            return false;
        }

        if (TiktokConnectionUtils.getClient() == null)
        {
            TiktokConnectionUtils.setupLiveClient(player,hostName);
        }

        TiktokConnectionUtils.connect();
        BlockPlaceListener.enableBlockPlacementListener();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Game successfully started on " + hostName + " hostname"));
        return true;
    }
}
