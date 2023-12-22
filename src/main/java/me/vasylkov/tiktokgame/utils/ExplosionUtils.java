package me.vasylkov.tiktokgame.utils;

import me.vasylkov.tiktokgame.TiktokGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ExplosionUtils
{
    public static void createCustomExplosion(Player player, int amount, float power)
    {
        new BukkitRunnable()
        {
            private int remaining = amount;
            @Override
            public void run()
            {
                Location playerLocation = player.getLocation();

                World world = player.getWorld();
                if (remaining <= 0)
                {
                    this.cancel();
                    return;
                }

                TNTPrimed tnt = (TNTPrimed) playerLocation.getWorld().spawnEntity(playerLocation, EntityType.PRIMED_TNT);
                tnt.setYield(power);
                tnt.setFuseTicks(15); // Устанавливаем время до взрыва динамита на 0, чтобы он взрывался сразу после появления


                remaining--;
            }
        }.runTaskTimer(JavaPlugin.getProvidingPlugin(TiktokGame.class), 0L, 6L); // 4 тика = 0.20 секунды
    }
}
