package me.vasylkov.tiktokgame.utils;

import io.github.jwdeveloper.tiktok.TikTokLive;
import io.github.jwdeveloper.tiktok.live.LiveClient;
import me.vasylkov.tiktokgame.listeners.TiktokListener;
import org.bukkit.entity.Player;

public class TiktokConnectionUtils
{
    private static LiveClient client;

    public static void setupLiveClient(Player player, String hostName)
    {
        client = TikTokLive.newClient(hostName).
                addListener(new TiktokListener(player))
                .build();
    }

    public static void connect()
    {
        if (client != null)
        {
            client.connect();
        }
    }

    public static void disconnect()
    {
        if (client != null)
        {
            client.disconnect();
        }
    }

    public static LiveClient getClient()
    {
        return client;
    }

    public static void setClient(LiveClient client)
    {
        TiktokConnectionUtils.client = client;
    }
}
