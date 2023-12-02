package me.vasylkov.tiktokgame.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RegionUtils
{
    private static ProtectedRegion getRegion(Player player, String regionName)
    {
        World world = player.getWorld();
        ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)).getRegion(regionName);
        if (region == null)
        {
            player.sendMessage("Region not created");
        }
        return region;
    }

    public static boolean checkRegionBlocks(Player player, String regionName)
    {
        ProtectedRegion region = getRegion(player, regionName);
        if (region == null)
        {
            return false;
        }

        World world = player.getWorld();
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++)
        {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++)
            {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)
                {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.AIR)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void removeAllRegionBlocks(Player player, String regionName)
    {
        ProtectedRegion region = getRegion(player, regionName);
        if (region == null)
        {
            return;
        }

        World world = player.getWorld();
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++)
        {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++)
            {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)
                {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(Material.AIR);
                }
            }
        }
    }
}
