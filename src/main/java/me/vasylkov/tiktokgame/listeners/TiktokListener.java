package me.vasylkov.tiktokgame.listeners;

import io.github.jwdeveloper.tiktok.annotations.TikTokEventHandler;
import io.github.jwdeveloper.tiktok.data.events.*;
import io.github.jwdeveloper.tiktok.data.events.common.TikTokEvent;
import io.github.jwdeveloper.tiktok.data.events.gift.TikTokGiftEvent;
import io.github.jwdeveloper.tiktok.data.events.social.TikTokFollowEvent;
import io.github.jwdeveloper.tiktok.data.events.social.TikTokShareEvent;
import io.github.jwdeveloper.tiktok.listener.TikTokEventListener;
import io.github.jwdeveloper.tiktok.live.LiveClient;
import me.vasylkov.tiktokgame.TiktokGame;
import me.vasylkov.tiktokgame.utils.CountdownTask;
import me.vasylkov.tiktokgame.utils.ExplosionUtils;
import me.vasylkov.tiktokgame.utils.GameProcessUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class TiktokListener implements TikTokEventListener
{
    private final Player player;
    private Queue<TikTokEvent> eventQueue = new LinkedList<>();
    private boolean isProcessing = false;

    public TiktokListener(Player player)
    {
        this.player = player;
    }

   /* @TikTokEventHandler
    public synchronized void onAnyEvent(LiveClient liveClient, TikTokEvent event)
    {
        if (event instanceof TikTokGiftEvent || event instanceof TikTokFollowEvent || event instanceof TikTokShareEvent
                || (event instanceof TikTokCommentEvent && ((TikTokCommentEvent) event).getUser().getName().equals(TiktokGame.getInstance().getConfig().getString("moderatorName"))))
        {
            eventQueue.add(event);
            if (!isProcessing)
            {
                processNextEvent();
            }
        }
    }*/

    @TikTokEventHandler
    public synchronized void onGift(LiveClient liveClient, TikTokGiftEvent event)
    {
        processEventAndAddToQueue(event);
    }

    @TikTokEventHandler
    public synchronized void onFollow(LiveClient liveClient, TikTokFollowEvent event)
    {
        processEventAndAddToQueue(event);
    }

    @TikTokEventHandler
    public synchronized void onShare(LiveClient liveClient, TikTokShareEvent event)
    {
        processEventAndAddToQueue(event);
    }

    @TikTokEventHandler
    public synchronized void onComment(LiveClient liveClient, TikTokCommentEvent event)
    {
        if (event.getUser().getName().equals(TiktokGame.getInstance().getConfig().getString("moderatorName")))
        {
            processEventAndAddToQueue(event);
        }
        else if (event.getText().equalsIgnoreCase("tnt"))
        {
            int chance = new Random().nextInt(100);

            if (chance <= 5)
            {
                processEventAndAddToQueue(event);
            }
        }
    }

    @TikTokEventHandler
    public synchronized void onConnect(LiveClient liveClient, TikTokConnectedEvent event)
    {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', TiktokGame.chatMessageTitle + "&aConnected to host!"));
    }

    @TikTokEventHandler
    public synchronized void onDisconnect(LiveClient liveClient, TikTokDisconnectedEvent event)
    {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', TiktokGame.chatMessageTitle + "&aDisconnected from host!"));

        if (TiktokGame.isGameStarted())
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', TiktokGame.chatMessageTitle + "&aTrying to reconnect..."));

            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

            liveClient.connect();
        }
    }

    @TikTokEventHandler
    public synchronized void onLiveEnded(LiveClient liveClient, TikTokLiveEndedEvent event)
    {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', TiktokGame.chatMessageTitle + "&aThe host has ended the live!"));
        GameProcessUtils.stopGame();
    }

    @TikTokEventHandler
    public synchronized void onError(LiveClient liveClient, TikTokErrorEvent event)
    {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', TiktokGame.chatMessageTitle + "&4ERROR! &aThe game cannot be continued/launched. Please check the config settings."));
        GameProcessUtils.stopGame();
    }

    private void processEventAndAddToQueue(TikTokEvent event)
    {
        eventQueue.add(event);
        if (!isProcessing)
        {
            processNextEvent();
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

        if (value != 0)
        {
            createExplosion(profileName, value, 5.0F);
        }

    }

    private int getEventValue(TikTokEvent event)
    {
        if (event instanceof TikTokGiftEvent)
        {
            return ((TikTokGiftEvent) event).getGift().getDiamondCost();
        }
        else if (event instanceof TikTokShareEvent)
        {
            return 2;
        }
        else if (event instanceof TikTokFollowEvent)
        {
            return 2;
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
        else if (event instanceof TikTokShareEvent)
        {
            return ((TikTokShareEvent) event).getUser().getProfileName();
        }
        else if (event instanceof TikTokFollowEvent)
        {
            return ((TikTokFollowEvent) event).getUser().getProfileName();
        }
        else
        {
            return ((TikTokCommentEvent) event).getUser().getProfileName();
        }
    }

    private void createExplosion(String profileName, int amount, float power)
    {
        World world = player.getWorld();
        world.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&c" + profileName + " &fSend &ex" + amount + " TNT"), "", 10, 20, 10);
        ExplosionUtils.createCustomExplosion(player, amount, power);
        isProcessing = false;
        processNextEvent();
    }
}
