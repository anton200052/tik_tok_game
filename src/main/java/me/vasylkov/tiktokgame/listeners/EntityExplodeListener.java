package me.vasylkov.tiktokgame.listeners;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class EntityExplodeListener implements Listener
{
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event)
    {
        if (event.getEntity() instanceof TNTPrimed)
        {
            Location loc = event.getLocation();
            Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            FireworkMeta meta = firework.getFireworkMeta();
            FireworkEffect effect1 = FireworkEffect.builder().flicker(true).trail(true).withColor(Color.RED).withFade(Color.ORANGE).with(FireworkEffect.Type.BALL_LARGE).build();
            FireworkEffect effect2 = FireworkEffect.builder().flicker(true).trail(true).withColor(Color.BLUE).withFade(Color.PURPLE).with(FireworkEffect.Type.BALL_LARGE).build();
            FireworkEffect effect3 = FireworkEffect.builder().flicker(true).trail(true).withColor(Color.GREEN).withFade(Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).build();

            meta.addEffect(effect1);
            meta.addEffect(effect2);
            meta.addEffect(effect3);

            firework.setFireworkMeta(meta);
            firework.detonate();
        }
    }
}
