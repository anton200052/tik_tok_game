package me.vasylkov.tiktokgame.listeners;

import me.vasylkov.tiktokgame.TiktokGame;
import me.vasylkov.tiktokgame.utils.CountdownTask;
import me.vasylkov.tiktokgame.utils.RegionUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockPlaceListener implements Listener
{
    private static CountdownTask countdownTask;
    private static boolean blockPlacementListened;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();


        if (blockPlacementListened)
        {
            boolean isRegionFull = RegionUtils.checkRegionBlocks(player, TiktokGame.getInstance().getConfig().getString("regionName"));

            if (isRegionFull)
            {
                countdownTask = new CountdownTask(player);
                countdownTask.runTaskTimer(TiktokGame.getInstance(), 0L, 25L);
            }
        }
    }


    public static boolean isBlockPlacementListened()
    {
        return blockPlacementListened;
    }

    public static void enableBlockPlacementListener()
    {
        blockPlacementListened = true;
    }

    public static void disableBlockPlacementListener()
    {
        blockPlacementListened = false;
    }

    public static CountdownTask getCountdownTask()
    {
        return countdownTask;
    }
}
