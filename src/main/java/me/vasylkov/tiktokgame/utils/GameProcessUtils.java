package me.vasylkov.tiktokgame.utils;

import me.vasylkov.tiktokgame.TiktokGame;
import me.vasylkov.tiktokgame.listeners.BlockPlaceListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameProcessUtils
{
    private static final String hostName = TiktokGame.getInstance().getConfig().getString("hostName");
    public static void startGame(Player player)
    {
        TiktokGame.setGameStarted(true);

        TiktokConnectionUtils.setupNewLiveClient(player, hostName);
        TiktokConnectionUtils.connect();
        BlockPlaceListener.enableBlockPlacementListener();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', TiktokGame.chatMessageTitle + "&aAttempting to connect to the hostname: &e" + hostName));
    }

    public static void stopGame()
    {
        TiktokGame.setGameStarted(false);

        CountdownTask countdownTask = BlockPlaceListener.getCountdownTask();
        if (countdownTask != null)
        {
            countdownTask.endGame();
        }
        TiktokConnectionUtils.disconnect();
    }

}
