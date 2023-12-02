package me.vasylkov.tiktokgame.utils;

import me.vasylkov.tiktokgame.TiktokGame;
import me.vasylkov.tiktokgame.listeners.BlockPlaceListener;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTask extends BukkitRunnable
{
    private final Player player;
    private int count;

    private boolean enabled = false;

    public CountdownTask(Player player)
    {
        this.player = player;
        this.count = 15;
    }

    @Override
    public void run()
    {
        enabled = true;

        if (BlockPlaceListener.isBlockPlacementListened())
        {
            BlockPlaceListener.disableBlockPlacementListener();
        }

        if (count <= 0)
        {
            wonGame();
        }
        else
        {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.0F, 1.0F);
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&e" + count), "", 10, 20, 10);
            count--;
        }
    }

    private void cancelAndRemoveBlocks()
    {
        RegionUtils.removeAllRegionBlocks(player, TiktokGame.getInstance().getConfig().getString("regionName"));
        enabled = false;
        if (!this.isCancelled())
        {
            this.cancel();
        }
    }

    public void resetTimer()
    {
        if (enabled)
        {
            BlockPlaceListener.enableBlockPlacementListener();
            this.cancel();
            enabled = false;}
    }

    public void wonGame()
    {
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&aSTREAMER WIN :)"), "", 10, 20, 10);
        BlockPlaceListener.enableBlockPlacementListener();
        cancelAndRemoveBlocks();
    }

    public void endGame()
    {
        BlockPlaceListener.disableBlockPlacementListener();
        cancelAndRemoveBlocks();
    }
}
