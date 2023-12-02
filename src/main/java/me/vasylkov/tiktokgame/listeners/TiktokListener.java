package me.vasylkov.tiktokgame.listeners;

import io.github.jwdeveloper.tiktok.annotations.TikTokEventHandler;
import io.github.jwdeveloper.tiktok.data.events.TikTokSubscribeEvent;
import io.github.jwdeveloper.tiktok.data.events.common.TikTokEvent;
import io.github.jwdeveloper.tiktok.data.events.common.TikTokHeaderEvent;
import io.github.jwdeveloper.tiktok.data.events.gift.TikTokGiftComboEvent;
import io.github.jwdeveloper.tiktok.data.events.gift.TikTokGiftEvent;
import io.github.jwdeveloper.tiktok.data.events.social.TikTokFollowEvent;
import io.github.jwdeveloper.tiktok.data.models.gifts.Gift;
import io.github.jwdeveloper.tiktok.data.models.users.User;
import io.github.jwdeveloper.tiktok.listener.TikTokEventListener;
import io.github.jwdeveloper.tiktok.live.LiveClient;
import me.vasylkov.tiktokgame.utils.CountdownTask;
import me.vasylkov.tiktokgame.utils.ExplosionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

public class TiktokListener implements TikTokEventListener
{
    private final Player player;
    private Queue<TikTokEvent> eventQueue = new LinkedList<>();
    private boolean isProcessing = false;

    public TiktokListener(Player player)
    {
        this.player = player;
    }

    @TikTokEventHandler
    public void onAnyEvent(LiveClient liveClient, TikTokEvent event)
    {
        if (event instanceof TikTokGiftEvent || event instanceof TikTokFollowEvent)
        {
            eventQueue.add(event);

            if (!isProcessing)
            {
                processNextEvent();
            }
        }
    }

    public void processNextEvent()
    {
        if (eventQueue.isEmpty()) // Если очередь пуста, то ничего не делаем
        {
            isProcessing = false;
            return;
        }

        isProcessing = true;

        TikTokEvent event = eventQueue.poll();

        int value = getEventValue(event);
        String profileName = getProfileName(event);

        CountdownTask countdownTask = BlockPlaceListener.getCountdownTask();
        if (countdownTask != null)
        {
            countdownTask.resetTimer();
        }

        createExplosion(profileName, value, 4.0F);

        /*switch (value)
        {
            case 1 -> createExplosion(profileName, 1, 4.0F);
            case 5 -> createExplosion(profileName, 5, 4.0F);
            case 10 -> createExplosion(profileName, 10, 4.0F);
            case 30 -> createExplosion(profileName, 30, 4.0F);
            default -> isProcessing = false;
        }*/
    }

    private int getEventValue(TikTokEvent event)
    {
        if (event instanceof TikTokGiftEvent)
        {
            return ((TikTokGiftEvent) event).getGift().getDiamondCost();
        }
        else
        {
            return 1;
        }
    }

    private String getProfileName(TikTokEvent event)
    {
        if (event instanceof TikTokGiftEvent)
        {
            return ((TikTokGiftEvent) event).getUser().getProfileName();
        }
        else
        {
            return ((TikTokFollowEvent) event).getUser().getProfileName();
        }
    }
    private void createExplosion(String profileName, int amount, float power)
    {
        World world = player.getWorld();
        world.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&a" + profileName + " &csend &4" + amount + " TNT"), "", 10, 20, 10);
        ExplosionUtils.createCustomExplosion(player, amount, power);
        isProcessing = false;
        processNextEvent();
    }
}
