package me.vasylkov.tiktokgame;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.github.jwdeveloper.tiktok.TikTokLive;
import me.vasylkov.tiktokgame.commands.EndGameCommand;
import me.vasylkov.tiktokgame.commands.StartGameCommand;
import me.vasylkov.tiktokgame.listeners.BlockPlaceListener;
import me.vasylkov.tiktokgame.listeners.TiktokListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TiktokGame extends JavaPlugin
{
    public static WorldGuardPlugin worldGuardPlugin;
    public static WorldEditPlugin worldEditPlugin;
    public static String chatMessageTitle = "&cVasylkov_Tiktok_Game: ";
    private static TiktokGame instance;

    public TiktokGame()
    {
        instance = this;
    }

    @Override
    public void onEnable()
    {
        setupWorldEditPlugin();
        setupWorldGuardPlugin();

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        Objects.requireNonNull(getCommand("startgame".toLowerCase())).setExecutor(new StartGameCommand());
        Objects.requireNonNull(getCommand("stopgame".toLowerCase())).setExecutor(new EndGameCommand());
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }

    private void setupWorldGuardPlugin()
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin instanceof WorldGuardPlugin)
        {
            worldGuardPlugin = (WorldGuardPlugin) plugin;
        }
        else
        {
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void setupWorldEditPlugin()
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin instanceof WorldEditPlugin)
        {
            worldEditPlugin = (WorldEditPlugin) plugin;
        }
        else
        {
            getServer().getPluginManager().disablePlugin(this);
        }
    }


    public static TiktokGame getInstance()
    {
        return instance;
    }

}
